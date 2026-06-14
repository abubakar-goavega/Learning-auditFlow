package com.abu.auditflow.scheduler.cleanup;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.abu.auditflow.auth.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetCleanupScheduler {

    private final PasswordResetTokenRepository repository;

    @Scheduled(cron = "0 30 15 * * *")
    public void cleanExpiredTokens() {

        log.info("Cleaning expired password reset tokens");

        repository.deleteExpired(Instant.now());

        log.info("Password reset cleanup done");
    }
}