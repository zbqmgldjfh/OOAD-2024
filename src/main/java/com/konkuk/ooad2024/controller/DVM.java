package com.konkuk.ooad2024.controller;

import com.konkuk.ooad2024.domain.*;
import com.konkuk.ooad2024.service.Beverages;
import com.konkuk.ooad2024.service.OtherDVMs;
import com.konkuk.ooad2024.service.PaymentMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller(value = "/")
public class DVM {
  private final Beverages beverages;
  private final OtherDVMs otherDVMs;
  private final Position position;
  private final PaymentMachine paymentMachine;
  private final Bank bank;
  private final Map<BeverageName, Beverage> myStock;
  private final String IS_NOT_PREPAY_POSSIBLE = "OtherDVM으로부터 선결제 불가능";
  @Autowired
  public DVM(Position myPosition, OtherDVMs otherDVMs, Beverages beverages,
             PaymentMachine paymentMachine, Bank bank, Map<BeverageName, Beverage> myStock) {
    this.position = myPosition;
    this.otherDVMs = otherDVMs;
    this.beverages = beverages;
    this.paymentMachine = paymentMachine;
    this.bank = bank;
    this.myStock = myStock;
  }

  @PostMapping("beverages")
  @ResponseBody
  public void selectBeverage(@RequestBody BeverageRequest request) {
    BeverageName bn = BeverageName.from(request.beverageId());
    int quantity = request.quantity();

    boolean haveStock = this.beverages.checkStock(bn, quantity);

    if (haveStock)
      // XXX: response 명세 필요
      // client와의 합의를 통해 즉시 결제가 가능함을 알리는 format을 정해야 합니다.
      // NOTE: client와 통신을 REST라고 가정하고 작성됨
      // WebSocket이라면 논의 내용이 달리질 수 있음
      return;

    // XXX: client와의 response 명세 필요
    Position nearest = this.otherDVMs.findNearestDVM(bn, quantity, this.position);
  }

  @PostMapping("eager-payments")
  @ResponseBody
  public void enterAccount(@RequestBody PaymentRequest request) {
    // TODO: balance check & eager-payment
    // `Bank`와 `PaymentMachine`에 위임 예정
  }

  @PostMapping("prepay")
  @ResponseBody
  public PaymentResponse prepay(@RequestBody PaymentRequest request) throws Exception {
    //accountId를 통해 계좌 잔액이 충분한지 확인
    long accountId = request.accountId();
    int beverageQuantity = request.quantity();
    BeverageName beverageName = BeverageName.from(request.beverageId()); //음료 이름
    long beveragePrice = myStock.get(beverageName).getPrice().getValue(); //음료 이름으로 가격 구하기
    long amount = beverageQuantity * beveragePrice;                     //총 결제할 금액 계산
    boolean haveBalance = bank.balanceCheck(accountId, amount);

    //계좌 잔액이 충분하다면 PaymentMachine에게 선결제 요청 #1 (boolean return)
    if(haveBalance){
      Position targetPosition = new Position(request.x(), request.y());
      Beverage beverageDTO = new Beverage(beverageName, (int)beveragePrice, beverageQuantity);
      boolean isPrepayPossible =  paymentMachine.prePayment(targetPosition, beverageDTO);
      if(isPrepayPossible){
        //TODO : 결제 진행
      }else {
        throw new Exception(IS_NOT_PREPAY_POSSIBLE);
      }
      return new PaymentResponse(isPrepayPossible, targetPosition.getXaxis(), targetPosition.getYaxis());
    }else{
      return new PaymentResponse(false, 0, 0);
    }
  }

  @PostMapping("paiedBeverages")
  @ResponseBody
  public PrePaidBeverageResponse gerPrePaidBeverage(@RequestBody PrePaidBeverageRequest request) {
    AuthenticationCode authenticationCode = AuthenticationCode.createRandomCode();
    Beverage beverage = paymentMachine.getPrePaiedBeverage(authenticationCode);
    boolean success = beverage==null ? false : true;
    String beverageId = null;
    int quantity = 0;

    if (success){
      beverageId = beverage.getItemCode();
      quantity = beverage.getStockValue();
    }

    return new PrePaidBeverageResponse(success, beverageId, quantity);
  }

  // XXX: need HELP!
  @MessageMapping("/")
  @SendTo("/topic")
  public boolean checkStock(@RequestBody BeverageRequest request) {
    return this.beverages.checkStock(BeverageName.from(request.beverageId()), request.quantity());
  }
}
