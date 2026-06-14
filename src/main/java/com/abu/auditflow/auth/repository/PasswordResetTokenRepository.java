package com.abu.auditflow.auth.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abu.auditflow.auth.entity.PasswordResetToken;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(UUID token);

    List<PasswordResetToken> findByUserId(Long userId);

    @Modifying
    @Query("""
            DELETE FROM PasswordResetToken p
            WHERE p.expiresAt < :now
            """)
    void deleteExpired(@Param("now") Instant now);
}