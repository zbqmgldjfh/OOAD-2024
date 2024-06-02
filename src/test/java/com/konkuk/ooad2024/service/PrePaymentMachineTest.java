package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.AuthenticationCode;
import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PrePaymentMachineTest {
  private PrePaymentMachine prePaymentMachine;
  private AuthenticationCodeGenerator authenticationCodeGenerator;

  @BeforeEach
  void setUp() {
    authenticationCodeGenerator = new AuthenticationCodeGenerator();
    prePaymentMachine = new PrePaymentMachine(authenticationCodeGenerator);
  }

  @Test
  void 인증코드_저장_및_반환_테스트(){
    // Given
    AuthenticationCode newCode = authenticationCodeGenerator.createAuthenticationCode();
    Beverage newBeverage = new Beverage(BeverageName.COKE, 1, 1);

    // When
    prePaymentMachine.storeBeverage(newCode, newBeverage);
    Beverage getBeverage = prePaymentMachine.getPrePaiedBeverage(newCode);

    // Then
    assertThat(newBeverage.getName()).isEqualTo(getBeverage.getName());
    assertThat(newBeverage.getPrice()).isEqualTo(getBeverage.getPrice());
    assertThat(newBeverage.getStock()).isEqualTo(getBeverage.getStock());
  }

}
