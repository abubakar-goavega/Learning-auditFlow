package com.abu.auditflow.auth.dto;

public record UserResponse(

        Long id,

        String username,

        String role,

        boolean enabled

) {
}