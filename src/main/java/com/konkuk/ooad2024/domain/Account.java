package com.konkuk.ooad2024.domain;

import java.util.concurrent.atomic.AtomicLong;

public class Account {

  private static AtomicLong idCounter = new AtomicLong();
  private final Long id;
  private Money balance;
  private Long lastUpdatedAt;

  public Account(Long id, Long balance) {
    this.id = id;
    this.balance = new Money(balance);
    this.lastUpdatedAt = System.currentTimeMillis();
  }

  public static Account createNewAccount(Long balance) {
    return new Account(idCounter.getAndIncrement(), balance);
  }

  public Long getId() {
    this.updateAccessTime();
    return this.id;
  }

  public Money getBalance() {
    this.updateAccessTime();
    return this.balance;
  }

  public Long increaseBalance(Money amount) {
    this.balance = this.balance.plus(amount);
    this.updateAccessTime();
    return this.lastUpdatedAt;
  }

  public Long decreaseBalance(Money money) {
    this.balance = this.balance.minus(money);
    this.updateAccessTime();
    return this.lastUpdatedAt;
  }

  private void updateAccessTime() {
    this.lastUpdatedAt = System.currentTimeMillis();
  }
}
