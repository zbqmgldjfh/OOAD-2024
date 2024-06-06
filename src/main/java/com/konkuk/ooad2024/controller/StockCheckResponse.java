package com.konkuk.ooad2024.controller;

public record StockCheckResponse(
    String msg_type, String src_id, String dst_id, StockCheckResponseContent msg_content) {}
