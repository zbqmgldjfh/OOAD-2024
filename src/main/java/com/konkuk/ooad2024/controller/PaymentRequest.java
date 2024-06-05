package com.konkuk.ooad2024.controller;

public record PaymentRequest(long accountId, String beverageId, int quantity, int x, int y) {}
