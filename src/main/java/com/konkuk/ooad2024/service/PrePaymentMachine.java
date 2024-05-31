package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrePaymentMachine {
  private final AuthenticationCodeGenerator authenticationCodeGenerator;
  private AuthenticationCode authenticationCode;

  public void prePayment(int position,int beverageId,int quantity){
    //TODO
  }

  public Beverage getPrePaiedBeverage(AuthenticationCode authenticationCode){
    Beverage beverage = null;
    //TODO

    return beverage;
  }
}
