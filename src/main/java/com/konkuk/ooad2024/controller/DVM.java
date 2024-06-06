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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
            StockCheckRequest request = mapper.readValue(reader, StockCheckRequest.class);
            String msg_type = request.msg_type();
            String src_id = request.src_id();
            String dst_id = request.dst_id();

            if ((!dst_id.equals("Team1") && !dst_id.equals("0")) || !msg_type.equals("req_stock")) {
              continue;
            }

            StockCheckRequestContent msg_content =
                mapper.readValue(reader, StockCheckRequest.class).msg_content();

            System.out.println("[SERVER] Received: " + request);

            boolean haveStock =
                this.beverages.checkStock(
                    BeverageName.from(msg_content.item_code()), msg_content.item_num());

            StockCheckResponseContent responseContent =
                new StockCheckResponseContent(
                    msg_content.item_code(),
                    (haveStock) ? msg_content.item_num() : 0,
                    this.position.getXaxis(),
                    this.position.getYaxis());

            StockCheckResponse response =
                new StockCheckResponse("resp_stock", "Team1", src_id, responseContent);

            writer.write(mapper.writeValueAsString(response));
            writer.newLine();
            writer.flush();
          } catch (Exception e) {
            PrepayResponse request = mapper.readValue(reader, PrepayResponse.class);
            String msg_type = request.msg_type();
            String src_id = request.src_id();
            String dst_id = request.dst_id();

            if ((!dst_id.equals("Team1") && !dst_id.equals("0"))
                || !msg_type.equals("req_prepay")) {
              continue;
            }

            System.out.println("[SERVER] Received: " + request);

            PrepayRequestContent msg_content =
                mapper.readValue(reader, PrepayRequest.class).msg_content();

            System.out.println("[SERVER] Received: " + request);

            BeverageName trgBeverage = BeverageName.from(msg_content.item_code());
            boolean haveStock = this.beverages.checkStock(trgBeverage, msg_content.item_num());
            if (haveStock) {
              this.beverages.reduce(trgBeverage, msg_content.item_num());
              this.paymentMachine.storeBeverage(
                  msg_content.cert_code(), new Beverage(trgBeverage, 0, msg_content.item_num()));
            }

            PrepayResponseContent responseContent =
                new PrepayResponseContent(
                    msg_content.item_code(), msg_content.item_num(), (haveStock) ? "T" : "F");

            PrepayResponse response =
                new PrepayResponse("resp_prepay", "Team1", src_id, responseContent);

            writer.write(mapper.writeValueAsString(response));
            writer.newLine();
            writer.flush();
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
