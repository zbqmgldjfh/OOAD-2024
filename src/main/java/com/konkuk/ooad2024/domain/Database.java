package com.konkuk.ooad2024.domain;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class Database {

    private final ConcurrentHashMap<Long, Account> db = new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        db.put(account.getId(), account);
    }

    public Money getBalanceById(long accountId) {
        return db.get(accountId).getBalance();
    }

    public boolean balanceCheck(long accountId, long money) {
        if(isNotExsistAccount(accountId)) {
            throw new IllegalArgumentException("존재하지 않는 계좌입니다.");
        }

        return this.getBalanceById(accountId)
                .isGreaterThanOrEqual(new Money(money));
    }

    public void decreaseBalanceById(long accountId, long money) {
        db.get(accountId).decreaseBalance(new Money(money));
    }

    private boolean isNotExsistAccount(long accountId) {
        return !db.containsKey(accountId);
    }
}
