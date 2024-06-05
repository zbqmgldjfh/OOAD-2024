package com.konkuk.ooad2024.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class AuthenticationCode {

  private static final String INPUT_INVALID_MESSAGE = "유효하지 않은 코드길이 입니다.";
  private static final int CODE_LENGTH = 10;

  private final String value;

  private AuthenticationCode(String value) {
    if (isInvalidInput(value)) {
      throw new IllegalArgumentException(INPUT_INVALID_MESSAGE);
    }
    this.value = value;
  }

  public static AuthenticationCode createRandomCode() {
    return new AuthenticationCode(UUID.randomUUID().toString().substring(0, CODE_LENGTH));
  }

  private boolean isInvalidInput(String value) {
    return value.length() != CODE_LENGTH;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthenticationCode that = (AuthenticationCode) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
