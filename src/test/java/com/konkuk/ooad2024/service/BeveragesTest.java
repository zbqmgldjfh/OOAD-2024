package com.konkuk.ooad2024.service;

import com.konkuk.ooad2024.domain.Beverage;
import com.konkuk.ooad2024.domain.BeverageName;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeveragesTest {
  @Test
  void 재고가_충분하면_재고_있음_테스트() {
    // given
    Map<BeverageName, Beverage> beverageMap =
        Map.ofEntries(
            new AbstractMap.SimpleEntry<>(
                BeverageName.COKE, new Beverage(BeverageName.COKE, 1_000, 5)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.BLACK_TEA, new Beverage(BeverageName.BLACK_TEA, 1_000, 3)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.ENERGY_DRINK, new Beverage(BeverageName.ENERGY_DRINK, 5_000, 15)));
    Beverages beverages = new Beverages(beverageMap);

    // when
    boolean actual = beverages.checkStock(BeverageName.BLACK_TEA, 3);

    // then
    assertThat(actual).isTrue();
  }

  @Test
  void 재고가_없으면_재고_없음_테스트() {
    // given
    Map<BeverageName, Beverage> beverageMap =
        Map.ofEntries(
            new AbstractMap.SimpleEntry<>(
                BeverageName.COKE, new Beverage(BeverageName.COKE, 1_000, 5)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.BLACK_TEA, new Beverage(BeverageName.BLACK_TEA, 1_000, 3)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.ENERGY_DRINK, new Beverage(BeverageName.ENERGY_DRINK, 5_000, 15)));
    Beverages beverages = new Beverages(beverageMap);

    // when
    boolean actual = beverages.checkStock(BeverageName.BLACK_TEA, 13);

    // then
    assertThat(actual).isFalse();
  }

  @Test
  void 재고가_충분하면_줄이기_테스트() {
    // given
    Map<BeverageName, Beverage> beverageMap =
        Map.ofEntries(
            new AbstractMap.SimpleEntry<>(
                BeverageName.COKE, new Beverage(BeverageName.COKE, 1_000, 5)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.BLACK_TEA, new Beverage(BeverageName.BLACK_TEA, 1_000, 3)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.ENERGY_DRINK, new Beverage(BeverageName.ENERGY_DRINK, 5_000, 15)));
    Beverages beverages = new Beverages(beverageMap);

    // when
    ThrowableAssert.ThrowingCallable actual = () -> beverages.reduce(BeverageName.ENERGY_DRINK, 10);

    // then
    assertThatCode(actual).doesNotThrowAnyException();
    assertThat(beverages.checkStock(BeverageName.ENERGY_DRINK, 5)).isTrue();
    assertThat(beverages.checkStock(BeverageName.ENERGY_DRINK, 6)).isFalse();
  }

  @Test
  void 재고가_부족하면_줄이면_오류_테스트() {
    // given
    Map<BeverageName, Beverage> beverageMap =
        Map.ofEntries(
            new AbstractMap.SimpleEntry<>(
                BeverageName.COKE, new Beverage(BeverageName.COKE, 1_000, 5)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.BLACK_TEA, new Beverage(BeverageName.BLACK_TEA, 1_000, 3)),
            new AbstractMap.SimpleEntry<>(
                BeverageName.ENERGY_DRINK, new Beverage(BeverageName.ENERGY_DRINK, 5_000, 15)));
    Beverages beverages = new Beverages(beverageMap);

    // when
    ThrowableAssert.ThrowingCallable actual = () -> beverages.reduce(BeverageName.COKE, 10);

    // then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }
}
