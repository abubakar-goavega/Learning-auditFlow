package com.abu.auditflow.user.dto;

public record UserDto(

        Long id,

        String username,

        String email,

        boolean enabled,

        String roleCode,

        String roleName
) {
}