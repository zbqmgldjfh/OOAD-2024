package com.konkuk.ooad2024.domain;

import java.text.DecimalFormat;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Money implements Comparable<Money> {

  private static final String INVALID_OPERATION_MESSAGE = "작은금액에서 큰 금액을 뺼 수 없습니다.";
  private static final String INVALID_INPUT_MESSAGE = "음수의 숫자를 받을 수 없습니다.";
  private static final String CURRENCY_UNIT = "원";
  private static final int ZERO_VALUE = 0;

  private final long value;

  public Money(long value) {
    validate(value);
    this.value = value;
  }

  public Money plus(Money otherMoney) {
    return new Money(this.value + otherMoney.value);
  }

  public Money minus(Money otherMoney) {
    if (this.value < otherMoney.value) {
      throw new IllegalArgumentException(INVALID_OPERATION_MESSAGE);
    }
    return new Money(this.value - otherMoney.value);
  }

  public Money multiply(int count) {
    return new Money(this.value * count);
  }

  public boolean isZero() {
    return this.value == ZERO_VALUE;
  }

  public boolean isGreaterThanOrEqual(Money money) {
    return this.compareTo(money) >= 0;
  }

  @Override
  public int compareTo(Money o) {
    long result = this.value - o.value;
    if (result > 0) {
      return 1;
    }
    if (result < 0) {
      return -1;
    }
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Money money = (Money) o;
    return Objects.equals(value, money.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    DecimalFormat decFormat = new DecimalFormat("###,###");
    return decFormat.format(this.value) + CURRENCY_UNIT;
  }

  private void validate(long value) {
    if (value < ZERO_VALUE) {
      throw new IllegalArgumentException(INVALID_INPUT_MESSAGE);
    }
  }
}
