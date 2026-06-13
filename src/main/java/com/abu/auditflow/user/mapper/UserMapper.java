package com.abu.auditflow.user.mapper;

import org.springframework.stereotype.Component;

import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(
            User user) {

        return new UserDto(

                user.getId(),

                user.getUsername(),

                user.isEnabled(),

                user.getRole().getCode(),

                user.getRole().getName());
    }
}