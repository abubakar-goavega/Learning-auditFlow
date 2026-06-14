package com.abu.auditflow.user.dto;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        Long roleId
) {}