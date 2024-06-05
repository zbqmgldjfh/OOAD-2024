package com.konkuk.ooad2024.domain;

import lombok.Synchronized;
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

  public boolean balanceCheck(Long accountId, Long amount) {
    return database.balanceCheck(accountId, amount);
  }

  @Synchronized
  public Long requestPayment(Long accountId, Long amount) {
    Long paymentTime = database.decreaseBalanceById(accountId, amount);

    PaymentHistory paymentHistory = new PaymentHistory(accountId, amount, paymentTime);
    savePaymentHistory(accountId, paymentHistory);

    return paymentHistory.getId();
  }

  @Synchronized
  boolean paymentCancel(Long accountId, Long paymentId) {
    if (isNotYetCreateFirstPayment(accountId)) {
      throw new IllegalArgumentException(NOT_EXIST_PAYMENT);
    }

    return paymentCancleByAccountAndPaymentId(accountId, paymentId);
  }

  private void savePaymentHistory(Long accountId, PaymentHistory paymentHistory) {
    histories.computeIfAbsent(accountId, id -> new ArrayList<>());
    histories.get(accountId).add(paymentHistory);
  }

  private boolean paymentCancleByAccountAndPaymentId(Long accountId, Long paymentId) {
    PaymentHistory findHistory = findTargetPayment(accountId, paymentId);
    boolean result = database.increaseBalanceById(accountId, findHistory.getAmount());
    histories.get(accountId).remove(findHistory);
    return result;
  }

  private PaymentHistory findTargetPayment(Long accountId, Long paymentId) {
    return histories.get(accountId).stream()
        .filter(paymentHistory -> paymentHistory.isSameId(paymentId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PAYMENT));
  }

  private boolean isNotYetCreateFirstPayment(Long accountId) {
    return histories.get(accountId) == null || histories.get(accountId).isEmpty();
  }
}
