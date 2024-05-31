package com.konkuk.ooad2024.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Bank {

    private static final String NOT_EXIST_PAYMENT = "존재하지 않는 결제입니다.";
    private final ConcurrentHashMap<Long, List<PaymentHistory>> histories = new ConcurrentHashMap<>();
    private final Database database;


    public Bank(Database database) {
        this.database = database;
    }

    boolean balanceCheck(Long accountId, Long amount) {
        return database.balanceCheck(accountId, amount);
    }

    Long requestPayment(Long accountId, Long amount) {
        Long paymentTime = database.decreaseBalanceById(accountId, amount);
        PaymentHistory paymentHistory = new PaymentHistory(accountId, amount, paymentTime);

        histories.computeIfAbsent(accountId, id -> new ArrayList<>());
        histories.get(accountId).add(paymentHistory);

        return paymentHistory.getId();
    }

    boolean paymentCancel(Long accountId, Long paymentId) {
        if(isNotYetCreateFirstPayment(accountId)) {
            throw new IllegalArgumentException(NOT_EXIST_PAYMENT);
        }

        PaymentHistory findHistory = histories.get(accountId)
                .stream().filter(paymentHistory -> paymentHistory.isSameId(paymentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PAYMENT));

        return database.increaseBalanceById(accountId, findHistory.getAmount());
    }

    private boolean isNotYetCreateFirstPayment(Long accountId) {
        return histories.get(accountId) == null || histories.get(accountId).isEmpty();
    }
}
