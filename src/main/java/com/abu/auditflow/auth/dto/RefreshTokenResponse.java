package com.abu.auditflow.auth.dto;

public record RefreshTokenResponse(
        Long userId,

        String accessToken,

        String tokenType,

        long expiresIn,

        String refreshToken
) {
}