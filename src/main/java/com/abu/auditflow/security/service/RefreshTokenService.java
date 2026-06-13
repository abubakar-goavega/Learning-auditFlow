package com.abu.auditflow.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.exception.ResourceNotFoundException;
import com.abu.auditflow.security.entity.RefreshToken;
import com.abu.auditflow.security.repository.RefreshTokenRepository;
import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.entity.User;
import com.abu.auditflow.user.mapper.UserMapper;
import com.abu.auditflow.user.repository.UserRepository;

@Service
@Transactional
public class RefreshTokenService {

        private final RefreshTokenRepository refreshTokenRepository;
        private final UserRepository userRepository;

        public RefreshTokenService(
                        RefreshTokenRepository refreshTokenRepository,
                        UserRepository userRepository) {

                this.refreshTokenRepository = refreshTokenRepository;
                this.userRepository = userRepository;
        }

        public UserDto getUser(@NonNull Long userId) {
                User user = userRepository
                                .findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found"));
                return new UserMapper().toDto(user);
        }

        /*
         * Creates a new refresh token.
         *
         * Default lifetime:
         * 30 days
         */
        public RefreshToken create(
                        @NonNull Long userId) {

                RefreshToken refreshToken = new RefreshToken();

                refreshToken.setUserId(userId);

                refreshToken.setToken(
                                UUID.randomUUID());

                refreshToken.setExpiresAt(
                                Instant.now()
                                                .plus(30, ChronoUnit.DAYS));

                refreshToken.setRevoked(false);

                return refreshTokenRepository.save(
                                refreshToken);
        }

        /*
         * Validates refresh token.
         *
         * Checks:
         * - Exists
         * - Not revoked
         * - Not expired
         */
        public RefreshToken validate(String token) {

                RefreshToken refreshToken = refreshTokenRepository
                                .findByToken(UUID.fromString(token))
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Refresh token not found"));

                if (refreshToken.isRevoked()) {
                        throw new IllegalStateException(
                                        "Refresh token revoked");
                }

                if (refreshToken.getExpiresAt()
                                .isBefore(Instant.now())) {
                        throw new IllegalStateException(
                                        "Refresh token expired");
                }

                return refreshToken;
        }

        /*
         * Removes all refresh tokens
         * belonging to a user.
         *
         * Useful for:
         * - logout everywhere
         * - password reset
         * - account lock
         */
        public void revokeAll(
                        @NonNull Long userId) {

                refreshTokenRepository
                                .deleteByUserId(userId);
        }

        @Transactional
        public void revoke(String token) {
                RefreshToken refreshToken = validate(token);

                refreshToken.setRevoked(true);

                refreshTokenRepository.save(
                                refreshToken);
        }
}