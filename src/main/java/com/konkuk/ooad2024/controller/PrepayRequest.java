package com.konkuk.ooad2024.controller;

public record PrepayRequest(
    String msg_type, String src_id, String dst_id, PrepayRequestContent msg_content) {}
