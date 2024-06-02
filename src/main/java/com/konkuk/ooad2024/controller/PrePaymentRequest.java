package com.konkuk.ooad2024.controller;

// NOTE: client와의 통신이 REST가 아닌 WebSocket이라면, accountId만으로 충분할 수 있음
// 또한, 원래 작성한 Class Diagram은 해당 형태를 띄고 있음
public record PrePaymentRequest(long accountId, int beverageId, int quantity) {}
