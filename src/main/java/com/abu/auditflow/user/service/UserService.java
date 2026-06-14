package com.abu.auditflow.user.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.exception.ResourceNotFoundException;
import com.abu.auditflow.user.dto.CreateUserRequest;
import com.abu.auditflow.user.dto.UpdateUserRequest;
import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.entity.User;
import com.abu.auditflow.user.mapper.UserMapper;
import com.abu.auditflow.user.repository.UserRepository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEnabled(true);

        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

    @Cacheable(value = "users", key = "#userId")
    @Transactional(readOnly = true)
    public UserDto getUserById(@NonNull Long userId) {

        log.info("Fetching user id={}", userId);

        User user = findEntityById(userId);

        return userMapper.toDto(user);
    }

    public User findEntityById(@NonNull Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    public void updatePassword(Long userId, String encodedPassword) {

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPasswordHash(encodedPassword);

        userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#userId")
    public UserDto updateUser(Long userId, UpdateUserRequest request) {

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.username() != null) {
            user.setUsername(request.username());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

    public void deleteUser(Long userId) {

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setDeletedAt(Instant.now());
        userRepository.save(user);
    }
}
