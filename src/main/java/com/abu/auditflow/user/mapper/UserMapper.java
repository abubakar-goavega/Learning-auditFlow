package com.abu.auditflow.user.mapper;

import org.springframework.stereotype.Component;

import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {

        String roleCode = null;
        String roleName = null;

        if (user.getRole() != null) {

            roleCode = user.getRole().getCode();
            roleName = user.getRole().getName();
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                roleCode,
                roleName);
    }
}