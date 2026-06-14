package com.abu.auditflow.user.dto;

public record UpdateUserRequest(
        String username,
        String email
) {}