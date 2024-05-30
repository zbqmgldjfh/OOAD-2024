package com.konkuk.ooad2024.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BeverageTest {

    @Test
    void 생성_테스트() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> new Beverage(BeverageName.AMERICANO, 1500, 10);

        // then
        assertThatCode(actual).doesNotThrowAnyException();
    }

    @Test
    void 음료_재고_감소_테스트() {
        // given
        Beverage beverage = new Beverage(BeverageName.AMERICANO, 1500, 10);

        // when
        beverage.decreaseStock(5);

        // then
        assertThat(beverage.getStockValue()).isEqualTo(5);
    }

    @Test
    void 음료_재고_실패_테스트() {
        // given
        Beverage beverage = new Beverage(BeverageName.AMERICANO, 1500, 10);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> beverage.decreaseStock(11);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
