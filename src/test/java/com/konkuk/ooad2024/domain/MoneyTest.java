package com.konkuk.ooad2024.domain;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {

    @Nested
    class 돈_생성 {
        @Test
        void 돈_생성_테스트() {
            // when
            ThrowingCallable actual = () -> new Money(1000);

            // then
            assertThatCode(actual).doesNotThrowAnyException();
        }

        @Test
        void 돈_음수_값_입력시_예외발생_테스트() {
            // when
            ThrowingCallable actual = () -> new Money(-1);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 돈_사칙연산 {
        @Test
        void 돈_더하기_테스트() {
            // given
            Money money1 = new Money(1000);
            Money money2 = new Money(12500);

            // when
            Money result = money1.plus(money2);

            // then
            assertThat(result).isEqualTo(new Money(13500));
        }

        @Test
        void 돈_빼기_테스트() {
            // given
            Money money1 = new Money(1000);
            Money money2 = new Money(12500);

            // when
            Money result = money2.minus(money1);

            // then
            assertThat(result).isEqualTo(new Money(11500));
        }

        @Test
        void 돈_곱하기_테스트() {
            // given
            Money money1 = new Money(1000);

            // when
            Money result = money1.multiply(3);

            // then
            assertThat(result).isEqualTo(new Money(3000));
        }

        @Test
        void 돈_작은수에서_큰수_빼기_예외_테스트() {
            // given
            Money money1 = new Money(1000);
            Money money2 = new Money(12500);

            // when
            ThrowingCallable actual = () -> money1.minus(money2);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 돈_비교 {
        @ParameterizedTest
        @MethodSource("moneyCompareInputProvider")
        void 돈_비교_테스트(int me, int other, int expected) {
            // given
            Money moneyOne = new Money(me);
            Money moneyTwo = new Money(other);

            // when
            int result = moneyOne.compareTo(moneyTwo);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("moneyGreaterOrEqualCompareInputProvider")
        void 같거나_큰_돈_비교_테스트(int me, int other, boolean expected) {
            // given
            Money moneyOne = new Money(me);
            Money moneyTwo = new Money(other);

            // when
            boolean result = moneyOne.isGreaterThanOrEqual(moneyTwo);

            // then
            assertThat(result).isEqualTo(expected);
        }

        private static Stream<Arguments> moneyCompareInputProvider() {
            return Stream.of(
                    Arguments.of(3, 4, -1),
                    Arguments.of(4, 3, 1),
                    Arguments.of(3, 6, -1),
                    Arguments.of(6, 3, 1),
                    Arguments.of(3, 3, 0)
            );
        }

        private static Stream<Arguments> moneyGreaterOrEqualCompareInputProvider() {
            return Stream.of(
                    Arguments.of(1000, 999, true),
                    Arguments.of(1000, 1000, true),
                    Arguments.of(1000, 1001, false)
            );
        }
    }

    @Test
    void 돈_출력_테스트() {
        // given
        Money money = new Money(1000);

        // when
        String result = money.toString();

        // then
        assertThat(result).isEqualTo("1,000원");
    }
}
