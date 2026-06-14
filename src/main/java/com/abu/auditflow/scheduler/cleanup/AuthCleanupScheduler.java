package com.abu.auditflow.scheduler.cleanup;

import com.abu.auditflow.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Runs every day at 3 AM
     */
    @Scheduled(cron = "0 0 15 * * *")
    public void cleanExpiredRefreshTokens() {

        log.info("Starting refresh token cleanup job");

        Instant now = Instant.now();

        refreshTokenRepository.deleteExpiredTokens(now);

        log.info("Completed refresh token cleanup job");
    }
}