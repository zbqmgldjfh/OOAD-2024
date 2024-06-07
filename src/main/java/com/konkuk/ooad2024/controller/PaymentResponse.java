package com.konkuk.ooad2024.controller;

public record PaymentResponse(boolean haveBalance, String authenticationCode) {}
