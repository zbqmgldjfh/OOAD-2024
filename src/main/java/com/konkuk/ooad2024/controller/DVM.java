package com.konkuk.ooad2024.controller;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import com.konkuk.ooad2024.domain.Position;
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

@Controller(value = "/")
public class DVM {
  private final Beverages beverages;
  private final OtherDVMs otherDVMs;
  private final Position position;
  private final PaymentMachine paymentMachine;

  @Autowired
  public DVM(Position myPosition, OtherDVMs otherDVMs, Beverages beverages, PaymentMachine paymentMachine) {
    this.position = myPosition;
    this.otherDVMs = otherDVMs;
    this.beverages = beverages;
    this.paymentMachine = paymentMachine;
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
  public void prepay(@RequestBody PaymentRequest request) {
    // TODO: pre-payments
    // `PaymentMachine`에 위임 예정
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
