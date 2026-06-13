package com.abu.auditflow.security.dto;

public record RefreshTokenResponse(
        Long userId,
        String accessToken,

        String tokenType,

        long expiresIn

) {
}