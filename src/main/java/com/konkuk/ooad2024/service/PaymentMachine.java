package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;

@RequiredArgsConstructor
@Service
public class PaymentMachine {
  private final EagerPaymentMachine eagerPaymentMachine; //즉시 결제 Machine
  private final PrePaymentMachine prePaymentMachine;     //선 결제 Machine

  public void prePayment(int position, Beverage beverage){
    prePaymentMachine.prePayment(position, beverage);
  }

  public void storeBeverage(AuthenticationCode authenticationCode, Beverage beverage) {
    //우리 DVM에 Other DVM으로 부터온 인증코드와 음료수를 저장
    prePaymentMachine.storeBeverage(authenticationCode, beverage);
  }

  public Beverage getPrePaiedBeverage(AuthenticationCode authenticationCode){
    //인증코드와 맞는 음료수 반환
    Beverage beverage = prePaymentMachine.getPrePaiedBeverage(authenticationCode);
    return beverage;
  }

}
