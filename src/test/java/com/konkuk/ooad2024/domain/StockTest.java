package com.konkuk.ooad2024.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @Test
    void 재고_생성_테스트() {
        // given
        Stock stock = new Stock(10);

        // when, then
        assertThat(stock).isEqualTo(new Stock(10));
    }

    @Test
    void 재고_비교_테스트() {
        // given
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(20);

        // when, then
        assertThat(stock1.compareTo(stock2)).isLessThan(0);
    }

    @Test
    void 재고_동등_테스트() {
        // given
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(10);

        // when, then
        Assertions.assertThat(stock1.equals(stock2)).isTrue();
    }

    @Test
    void 재고_출력_테스트() {
        // given
        Stock stock = new Stock(10);

        // when, then
        Assertions.assertThat(stock.toString()).isEqualTo("10");
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 101 })
    void 재고_생성_실패_테스트(int input) {
        // when, then
        assertThatThrownBy(() -> new Stock(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
