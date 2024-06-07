package com.konkuk.ooad2024.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PrePaymentResponseDto {
  private final boolean isPrepayPossible;
  private final String authenticationCode;
}
