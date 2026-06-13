package com.abu.auditflow.security.dto;

public record UserResponse(

        Long id,

        String username,

        String role,

        boolean enabled

) {
}