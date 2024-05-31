package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;

@RequiredArgsConstructor
@Service
public class PaymentMachine {
  private final EagerPaymentMachine eagerPaymentMachine;
  private final PrePaymentMachine prePaymentMachine;

  public void prePayment(int position,int beverageId,int quantity){
    prePaymentMachine.prePayment(position,beverageId,quantity);
  }
  public Beverage getPrePaiedBeverage(AuthenticationCode authenticationCode){
    Beverage beverage = prePaymentMachine.getPrePaiedBeverage(authenticationCode);

    return beverage;
  }
}
