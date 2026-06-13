package com.abu.auditflow.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.exception.ResourceNotFoundException;
import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.entity.User;
import com.abu.auditflow.user.mapper.UserMapper;
import com.abu.auditflow.user.repository.UserRepository;

import lombok.NonNull;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUser(@NonNull Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"));
        return new UserMapper().toDto(user);
    }
}
