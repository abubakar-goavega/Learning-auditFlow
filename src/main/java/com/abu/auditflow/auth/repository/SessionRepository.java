package com.abu.auditflow.auth.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abu.auditflow.auth.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    long countByUserIdAndRevokedFalse(Long userId);

    @Modifying
    @Query("""
            DELETE FROM Session s
            WHERE s.lastAccessedAt < :cutoff
            """)
    void deleteInactiveSessions(@Param("cutoff") Instant cutoff);

    List<Session> findByUserIdAndRevokedFalse(Long userId);
}
