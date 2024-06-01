package com.konkuk.ooad2024.domain;

import java.util.concurrent.atomic.AtomicLong;

public class PaymentHistory {

    private static final AtomicLong idCounter = new AtomicLong();
    private final Long id;
    private final Long accountId;
    private final Money amount;
    private final Long paymentTime;

    public PaymentHistory(Long accountId, Long amount, Long paymentTime) {
        this.id = idCounter.getAndIncrement();
        this.accountId = accountId;
        this.amount = new Money(amount);
        this.paymentTime = paymentTime;
    }

    public Long getId() {
        return id;
    }

    public boolean isSameId(Long paymentId) {
        return this.id.equals(paymentId);
    }

    public Money getAmount() {
        return this.amount;
    }
}
