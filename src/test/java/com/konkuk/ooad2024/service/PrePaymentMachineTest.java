package com.konkuk.ooad2024.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.konkuk.ooad2024.domain.*;
import com.konkuk.ooad2024.dto.PrePaymentResponseDto;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PrePaymentMachineTest {

  private PrePaymentMachine prePaymentMachine;
  private AuthenticationCodeGenerator anotherCodeGenerator;
  @Mock private AuthenticationCodeGenerator authenticationCodeGenerator;
  @Mock private OtherDVMs otherDVMs;
  @Mock private OtherDVM otherDVM;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    anotherCodeGenerator = new AuthenticationCodeGenerator();
    prePaymentMachine = new PrePaymentMachine(authenticationCodeGenerator, otherDVMs);
  }

  @Test
  void PrePaymentMachine_선결제_테스트() throws IOException {
    // Given
    Position position = new Position(1, 1);
    Beverage beverage = new Beverage(BeverageName.COKE, 500, 10);
    AuthenticationCode authCode = anotherCodeGenerator.createAuthenticationCode();

    // When
    when(authenticationCodeGenerator.createAuthenticationCode()).thenReturn(authCode);
    when(otherDVMs.findByPosition(any(Position.class))).thenReturn(otherDVM);
    when(otherDVM.prepay(any(Beverage.class), any(AuthenticationCode.class), any(Position.class)))
        .thenReturn(true);
    PrePaymentResponseDto result = prePaymentMachine.prePayment(position, beverage);

    // Then
    assertThat(result.isPrepayPossible()).isTrue();
    assertThat(result.getAuthenticationCode()).isEqualTo(authCode.getValue());
  }

  @Test
  void 인증코드_저장_및_반환_테스트() {
    // Given
    String newCode = "abcdefghij";
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
