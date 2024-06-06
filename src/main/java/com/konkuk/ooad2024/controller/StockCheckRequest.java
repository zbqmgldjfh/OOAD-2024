package com.konkuk.ooad2024.controller;

public record StockCheckRequest(
    String msg_type, String src_id, String dst_id, StockCheckRequestContent msg_content) {}
