package com.konkuk.ooad2024.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class AuthenticationCodeTest {

  @Test
  void 인증코드_생성_테스트() {
    // given
    ThrowableAssert.ThrowingCallable actual = () -> AuthenticationCode.createRandomCode();

    // when, then
    assertThatCode(actual).doesNotThrowAnyException();
  }

  @Test
  void 인증코드_길이_확인_테스트() {
    // given
    AuthenticationCode authenticationCode = AuthenticationCode.createRandomCode();

    // when, then
    Assertions.assertThat(authenticationCode.toString().length()).isEqualTo(10);
  }
}
