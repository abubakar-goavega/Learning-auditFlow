package com.abu.auditflow.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.auth.entity.PasswordResetToken;
import com.abu.auditflow.auth.repository.PasswordResetTokenRepository;
import com.abu.auditflow.user.entity.User;
import com.abu.auditflow.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserService userService,
            SessionService sessionService,
            PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userService = userService;
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgotPassword(String email) {

        User user = userService.findEntityByEmail(email);

        if (user == null) {
            return; // silent fail (security best practice)
        }

        UUID token = UUID.randomUUID();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUserId(user.getId());
        prt.setToken(token);
        prt.setExpiresAt(Instant.now().plus(Duration.ofMinutes(30)));
        prt.setUsed(false);
        prt.setCreatedAt(Instant.now());

        passwordResetTokenRepository.save(prt);

        log.info("RESET LINK: http://localhost:8080/reset-password?token={}", token);
    }

    public void resetPassword(UUID token, String newPassword) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = userService.findEntityById(resetToken.getUserId());
        Long userId = user.getId();

        if (userId == null) {
            throw new IllegalStateException("User ID cannot be null");
        }

        userService.updatePassword(userId, passwordEncoder.encode(newPassword));

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        
        sessionService.revokeAll(userId);
    }
}
