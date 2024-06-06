package com.konkuk.ooad2024.controller;

public record PrepayResponse(
    String msg_type, String src_id, String dst_id, PrepayResponseContent msg_content) {}
