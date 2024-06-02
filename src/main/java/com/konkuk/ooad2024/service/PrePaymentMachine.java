package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PrePaymentMachine {
  private final AuthenticationCodeGenerator authenticationCodeGenerator;
  private final Map<AuthenticationCode, Beverage> codeToBeverageMap = new HashMap<>();
  private static final String INVALID_AUTH_CODE_MESSAGE = "유효하지 않은 인증 코드입니다.";

  public void prePayment(int position, Beverage beverage){
    //새 인증코드 생성
    AuthenticationCode newCode = authenticationCodeGenerator.createAuthenticationCode();

    //다른 DVM으로 인증코드와 음료수 정보 보내기
    //TODO
  }

  public void storeBeverage(AuthenticationCode authenticationCode, Beverage beverage) {
    codeToBeverageMap.put(authenticationCode, beverage);
  }

  public Beverage getPrePaiedBeverage(AuthenticationCode authenticationCode){
    //HashMap에 해당 인증코드가 존재하는지 확인
    Beverage beverage = codeToBeverageMap.get(authenticationCode);
    if (beverage == null) {
      throw new IllegalArgumentException(INVALID_AUTH_CODE_MESSAGE);
    }

    return beverage;
  }
}
