package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.Position;
import com.konkuk.ooad2024.dto.PrePaymentResponseDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentMachine {
  private final PrePaymentMachine prePaymentMachine; // 선 결제 Machine

  public PrePaymentResponseDto prePayment(Position position, Beverage beverage) throws IOException {
    // #2
    return prePaymentMachine.prePayment(position, beverage);
  }

  public void storeBeverage(String authenticationCode, Beverage beverage) {
    // 우리 DVM에 Other DVM으로 부터온 인증코드와 음료수를 저장
    prePaymentMachine.storeBeverage(authenticationCode, beverage);
  }

  public Beverage getPrePaiedBeverage(String authenticationCode) {
    // 인증코드와 맞는 음료수 반환
    Beverage beverage = prePaymentMachine.getPrePaiedBeverage(authenticationCode);
    return beverage;
  }
}
