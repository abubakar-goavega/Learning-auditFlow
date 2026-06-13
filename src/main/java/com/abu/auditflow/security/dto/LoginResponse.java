package com.abu.auditflow.security.dto;

public record LoginResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn,

        UserResponse user

) {
}