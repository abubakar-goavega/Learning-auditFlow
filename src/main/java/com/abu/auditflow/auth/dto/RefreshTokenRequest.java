package com.abu.auditflow.auth.dto;

public record RefreshTokenRequest(
        Long sessionId,
        String refreshToken) {

}
