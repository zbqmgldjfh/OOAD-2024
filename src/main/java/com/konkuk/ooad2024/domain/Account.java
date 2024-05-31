package com.konkuk.ooad2024.domain;

public class Account {

    private static Long idCounter = 0L;
    private final Long id;
    private Money balance;
    private Long lastUpdatedAt;

    public Account(Long id, Long balance) {
        this.id = id;
        this.balance = new Money(balance);
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    public static Account createNewAccount(Long balance) {
        return new Account(idCounter++, balance);
    }

    public Long getId() {
        this.updateAccessTime();
        return this.id;
    }

    public Money getBalance() {
        this.updateAccessTime();
        return this.balance;
    }

    public void decreaseBalance(Money money) {
        this.updateAccessTime();
        this.balance = this.balance.minus(money);
    }

    private void updateAccessTime() {
        this.lastUpdatedAt = System.currentTimeMillis();
    }
}
