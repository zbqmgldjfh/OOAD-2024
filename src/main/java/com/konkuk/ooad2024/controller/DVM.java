package com.konkuk.ooad2024.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.ooad2024.domain.*;
import com.konkuk.ooad2024.dto.PrePaymentResponseDto;
import com.konkuk.ooad2024.service.Beverages;
import com.konkuk.ooad2024.service.OtherDVMs;
import com.konkuk.ooad2024.service.PaymentMachine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DVM implements Runnable {
  private final Beverages beverages;
  private final OtherDVMs otherDVMs;
  private final Position position;
  private final PaymentMachine paymentMachine;
  private final Bank bank;
  private static final String IS_NOT_PREPAY_POSSIBLE = "OtherDVM으로부터 선결제 불가능";

  @Autowired
  public DVM(
      Position myPosition,
      OtherDVMs otherDVMs,
      Beverages beverages,
      PaymentMachine paymentMachine,
      Bank bank) {
    this.position = myPosition;
    this.otherDVMs = otherDVMs;
    this.beverages = beverages;
    this.paymentMachine = paymentMachine;
    this.bank = bank;
  }

  @PostMapping("beverages")
  @ResponseBody
  public BeverageResponse selectBeverage(@RequestBody BeverageRequest request) {
    BeverageName bn = BeverageName.from(request.beverageId());
    int quantity = request.quantity();

    boolean haveStock = this.beverages.checkStock(bn, quantity);

    if (haveStock) return new BeverageResponse(true, null, null);

    Position nearest = this.otherDVMs.findNearestDVM(bn, quantity, this.position);
    if (nearest == null) return new BeverageResponse(false, null, null);

    return new BeverageResponse(true, nearest.getYaxis(), nearest.getYaxis());
  }

  @PostMapping("eager-payments")
  @ResponseBody
  public PaymentResponse enterAccount(@RequestBody PaymentRequest request) {
    // accountId를 통해 계좌 잔액이 충분한지 확인
    long accountId = request.accountId();
    int beverageQuantity = request.quantity();
    BeverageName beverageName = BeverageName.from(request.beverageId()); // 음료 이름
    long beveragePrice = this.beverages.findPriceByName(beverageName);
    long amount = beverageQuantity * beveragePrice; // 총 결제할 금액 계산

    boolean haveBalance = bank.balanceCheck(accountId, amount);

    // 계좌에 잔액이 있다면 즉시 결제 (계좌에 금액 차감, 음료수 개수 차감)
    // 재고 확인 후 즉시 결제를 하는 것이라고 생각해, 잔액만 있다면 무조건 즉시 결제 성공
    // 계좌에 잔액이 없거나 User가 잘못된 계좌 정보를 입력했을 때는 클라이언트에게 false return
    if (haveBalance) {
      bank.requestPayment(accountId, amount);
      beverages.reduce(beverageName, beverageQuantity);
      return new PaymentResponse(true, null);
    } else {
      return new PaymentResponse(false, null);
    }
  }

  @PostMapping("prepay")
  @ResponseBody
  public PaymentResponse prepay(@RequestBody PaymentRequest request) throws Exception {
    // accountId를 통해 계좌 잔액이 충분한지 확인
    long accountId = request.accountId();
    int beverageQuantity = request.quantity();
    BeverageName beverageName = BeverageName.from(request.beverageId()); // 음료 이름
    long beveragePrice = this.beverages.findPriceByName(beverageName);
    long amount = beverageQuantity * beveragePrice; // 총 결제할 금액 계산

    boolean haveBalance = bank.balanceCheck(accountId, amount);

    // 계좌 잔액이 충분하다면 PaymentMachine에게 선결제 요청 #1 (boolean return)
    if (haveBalance) {
      Position targetPosition =
          this.otherDVMs.findByPosition(new Position(request.x(), request.y())).getPosition();
      Beverage beverageDTO = new Beverage(beverageName, (int) beveragePrice, beverageQuantity);

      PrePaymentResponseDto prePaymentResponseDto =
          paymentMachine.prePayment(targetPosition, beverageDTO);

      boolean isPrepayPossible = prePaymentResponseDto.isPrepayPossible();
      String authenticationCode = prePaymentResponseDto.getAuthenticationCode();

      if (isPrepayPossible) {
        // 계좌에 잔액도 있고 Other DVM에서 선결제 여부도 true이면 결제 진행
        bank.requestPayment(accountId, amount);
      } else {
        throw new Exception(IS_NOT_PREPAY_POSSIBLE);
      }
      return new PaymentResponse(isPrepayPossible, authenticationCode);
    } else {
      return new PaymentResponse(false, null);
    }
  }

  @PostMapping("paiedBeverages")
  @ResponseBody
  public PrePaidBeverageResponse gerPrePaidBeverage(@RequestBody PrePaidBeverageRequest request) {
    String authenticationCode = request.authenticationCode();
    Beverage beverage = paymentMachine.getPrePaiedBeverage(authenticationCode);
    boolean success = beverage == null ? false : true;
    String beverageId = null;
    int quantity = 0;

    if (success) {
      beverageId = beverage.getItemCode();
      quantity = beverage.getStockValue();
    }

    return new PrePaidBeverageResponse(success, beverageId, quantity);
  }

  @PostConstruct
  public void init() {
    Thread thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {
    ObjectMapper mapper = new ObjectMapper();

    try (ServerSocket serverSocket = new ServerSocket(9999)) {
      System.out.println("Socket server started on port 9999");

      while (true) {
        try (Socket clientSocket = serverSocket.accept();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
          try {
            String requestLine = reader.readLine();
            System.out.println(requestLine);
            if (requestLine == null) continue;

            Map<String, Object> requestMap = mapper.readValue(requestLine, Map.class);
            Map<String, Object> msgContent = (Map<String, Object>) requestMap.get("msg_content");
            String msg_type = (String) requestMap.get("msg_type");
            String src_id = (String) requestMap.get("src_id");
            //            String dst_id = (String) requestMap.get("dst_id");

            //            if ((!dst_id.equals("Team1") && !dst_id.equals("0"))) {
            //              continue;
            //            }

            System.out.println("[SERVER] Received: " + requestMap);

            Map<String, Object> response = new HashMap<>();
            if (msg_type.equals("req_stock")) {
              String item_code = (String) msgContent.get("item_code");
              int item_num = (int) msgContent.get("item_num");

              boolean haveStock = this.beverages.checkStock(BeverageName.from(item_code), item_num);

              response.put("msg_type", "resp_stock");
              response.put("src_id", "Team1");
              response.put("dst_id", src_id);
              response.put(
                  "msg_content",
                  Map.of(
                      "item_code",
                      item_code,
                      "item_num",
                      (haveStock) ? item_num : 0,
                      "x",
                      this.position.getXaxis(),
                      "y",
                      this.position.getYaxis()));
            } else if (msg_type.equals("req_prepay")) {
              String item_code = (String) msgContent.get("item_code");
              int item_num = (int) msgContent.get("item_num");
              String cert_code = (String) msgContent.get("cert_code");

              BeverageName trgBeverage = BeverageName.from(item_code);
              boolean haveStock = this.beverages.checkStock(trgBeverage, item_num);
              if (haveStock) {
                this.beverages.reduce(trgBeverage, item_num);
                this.paymentMachine.storeBeverage(
                    cert_code, new Beverage(trgBeverage, 0, item_num));
              }

              response.put("msg_type", "resp_prepay");
              response.put("src_id", "Team1");
              response.put("dst_id", src_id);
              response.put(
                  "msg_content",
                  Map.of(
                      "item_code",
                      item_code,
                      "item_num",
                      item_num,
                      "availability",
                      (haveStock) ? "T" : "F"));
            } else {
              System.out.println("[SERVER] Invalid message type: " + msg_type);
              continue;
            }

            System.out.println("[SERVER] Sent: " + response);

            writer.write(mapper.writeValueAsString(response));
            writer.newLine();
            writer.flush();
          } catch (IOException ignored) {
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
