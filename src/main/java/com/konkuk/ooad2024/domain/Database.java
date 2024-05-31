package com.konkuk.ooad2024.domain;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class Database {

    private static final String NOT_EXIST_ACCOUNT = "존재하지 않는 계좌입니다.";
    private static final ConcurrentHashMap<Long, Account> db = new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        db.put(account.getId(), account);
    }

    public Money getBalanceById(long accountId) {
        return db.get(accountId).getBalance();
    }

    public boolean balanceCheck(long accountId, long money) {
        if(isNotExistAccount(accountId)) {
            throw new IllegalArgumentException(NOT_EXIST_ACCOUNT);
        }

        return this.getBalanceById(accountId)
                .isGreaterThanOrEqual(new Money(money));
    }

    public boolean increaseBalanceById(Long accountId, Money amount) {
        if(isNotExistAccount(accountId)) {
            throw new IllegalArgumentException(NOT_EXIST_ACCOUNT);
        }

        try{
            db.get(accountId).increaseBalance(amount);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Long decreaseBalanceById(long accountId, long money) {
        if(isNotExistAccount(accountId)) {
            throw new IllegalArgumentException(NOT_EXIST_ACCOUNT);
        }

        return db.get(accountId).decreaseBalance(new Money(money));
    }

    private boolean isNotExistAccount(long accountId) {
        return !db.containsKey(accountId);
    }
}
