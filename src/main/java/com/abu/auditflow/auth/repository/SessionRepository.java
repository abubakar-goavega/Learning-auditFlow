package com.abu.auditflow.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abu.auditflow.auth.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUserId(Long userId);

    void deleteByUserId(Long userId);
    long countByUserIdAndRevokedFalse(Long userId);
}
