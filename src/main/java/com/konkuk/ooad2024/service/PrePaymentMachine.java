package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PrePaymentMachine {
  private final AuthenticationCodeGenerator authenticationCodeGenerator;
  private final OtherDVM otherDVM; //
  private final Map<AuthenticationCode, Beverage> codeToBeverageMap = new HashMap<>();
  private static final String INVALID_AUTH_CODE_MESSAGE = "유효하지 않은 인증 코드입니다.";

  public boolean prePayment(Position position, Beverage beverage)throws IOException {
    // 새 인증코드 생성
    AuthenticationCode newCode = authenticationCodeGenerator.createAuthenticationCode();

    // #2 소켓 통신은 otherDVM에서진행, otherDVM에서는 다른 DVM이 선결제가 가능한지 확인 !
    // 이 결과가 DVM까지 연결되어 Return !!
    return otherDVM.prepay(beverage, newCode, position);
  }

  public void storeBeverage(AuthenticationCode authenticationCode, Beverage beverage) {
    codeToBeverageMap.put(authenticationCode, beverage);
  }

  public Beverage getPrePaiedBeverage(AuthenticationCode authenticationCode) {
    // HashMap에 해당 인증코드가 존재하는지 확인
    Beverage beverage = codeToBeverageMap.get(authenticationCode);
    if (beverage == null) {
      throw new IllegalArgumentException(INVALID_AUTH_CODE_MESSAGE);
    }

    return beverage;
  }
}
