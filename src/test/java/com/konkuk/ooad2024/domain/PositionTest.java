package com.konkuk.ooad2024.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PositionTest {
  @Test
  void 생성_테스트() {
    // when
    ThrowableAssert.ThrowingCallable actual = () -> new Position(20, 30);

    // then
    assertThatCode(actual).doesNotThrowAnyException();
  }

  @Test
  void 음수_좌표로_생성하면_생성_실패_테스트() {
    // when
    ThrowableAssert.ThrowingCallable actual = () -> new Position(-20, 30);

    // then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 너무큰_좌표로_생성하면_생성_실패_테스트() {
    // when
    ThrowableAssert.ThrowingCallable actual = () -> new Position(120, 30);

    // then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 거리_계산_테스트() {
    // given
    Position p1 = new Position(1, 5);
    Position p2 = new Position(3, 4);

    // when
    double actual = p1.getDistance(p2);

    // then
    assertThat(actual).isEqualTo(Math.sqrt(Math.pow(1 - 3, 2) + Math.pow(5 - 4, 2)));
  }
}
