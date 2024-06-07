package com.konkuk.ooad2024.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseTest {

  Database db;

  private static Stream<Arguments> balanceCheckTestInput() {
    return Stream.of(
        Arguments.of(10000L, true), Arguments.of(0L, true), Arguments.of(10001L, false));
  }

  @BeforeEach
  void setUp() {
    db = new Database();
  }

  @Test
  void 존재하지않는_계좌를_확인하는경우_예외가_발생한다() {
    // given
    db.addAccount(new Account(0L, 10000L));
    Long invalidId = -1L;

    // when
    ThrowableAssert.ThrowingCallable actual = () -> db.balanceCheck(invalidId, 10000L);

    // then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @MethodSource("balanceCheckTestInput")
  void 계좌_차감_가능_확인_테스트(Long money, boolean expected) {
    // given
    db.addAccount(new Account(0L, 10000L));

    // when
    boolean result = db.balanceCheck(0L, money);

    // then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void 계좌_차감_테스트() {
    // given
    Account account = new Account(0L, 10000L);
    db.addAccount(account);

    // when
    db.decreaseBalanceById(0L, 5000L);

    // then
    assertThat(db.getBalanceById(0L)).isEqualTo(new Money(5000L));
  }

  @Test
  void 계좌의_잔액보다_더_많은_금액을_차감하는_경우_예외가_발생한다() {
    // given
    db.addAccount(new Account(0L, 10000L));

    // when
    ThrowableAssert.ThrowingCallable actual = () -> db.decreaseBalanceById(0L, 10001L);

    // then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }
}
