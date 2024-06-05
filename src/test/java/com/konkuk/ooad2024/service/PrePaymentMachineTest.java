package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PrePaymentMachineTest {
  private PrePaymentMachine prePaymentMachine;
  private AuthenticationCodeGenerator authenticationCodeGenerator;
  private OtherDVM otherDVM;
  private OtherDVMs otherDVMS;

  @BeforeEach
  void setUp() {
    authenticationCodeGenerator = new AuthenticationCodeGenerator();
    Position position = new Position(1, 1);
    otherDVM = new OtherDVM(position, "127.0.0.2", 8080, "Team3");
    otherDVMS = new OtherDVMs(Map.of("Team3", otherDVM));
    prePaymentMachine = new PrePaymentMachine(authenticationCodeGenerator, otherDVMS);
  }

  @Test
  void 인증코드_저장_및_반환_테스트() {
    // Given
    String newCode = authenticationCodeGenerator.createAuthenticationCode().getValue();
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
