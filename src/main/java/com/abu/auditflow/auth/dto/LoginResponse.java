package com.abu.auditflow.auth.dto;

public record LoginResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn,

        UserResponse user

) {
}