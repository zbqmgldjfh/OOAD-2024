package com.konkuk.ooad2024.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTest {

    Bank bank;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        database.addAccount(new Account(0L, 10000L));
        bank = new Bank(database);
    }

    @Test
    void 은행_계좌의_돈_감소_테스트() {
        // when
        Long paymentId = bank.requestPayment(0L, 6000l);

        // then
        Assertions.assertThat(bank.balanceCheck(0L, 4000l)).isTrue();
    }

    @Test
    void 은행_계좌의_결제_취소_테스트() {
        // given
        Long paymentId = bank.requestPayment(0L, 6000l);

        // when
        bank.paymentCancel(0L, paymentId);

        // then
        Assertions.assertThat(bank.balanceCheck(0L, 10000L)).isTrue();
    }
}
