package com.abu.auditflow.auth.service;

import com.abu.auditflow.auth.entity.Session;
import com.abu.auditflow.auth.repository.SessionRepository;
import com.abu.auditflow.exception.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(
            SessionRepository sessionRepository) {

        this.sessionRepository = sessionRepository;
    }

    public List<Session> getActiveSessions(Long userId) {
        return sessionRepository.findByUserIdAndRevokedFalse(userId);
    }

    public Session create(
            @NonNull Long userId,
            String userAgent,
            String ipAddress) {

        Session session = new Session();

        session.setUserId(userId);
        session.setUserAgent(userAgent);
        session.setIpAddress(ipAddress);
        session.setRevoked(false);

        return sessionRepository.save(session);
    }

    public Session validate(
            @NonNull Long sessionId) {

        Session session = sessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Session not found"));

        if (session.isRevoked()) {
            throw new IllegalStateException(
                    "Session revoked");
        }

        return session;
    }

    public void revoke(
            @NonNull Long sessionId) {

        Session session = validate(sessionId);

        session.setRevoked(true);

        sessionRepository.save(session);
    }

    public void revokeAll(
            @NonNull Long userId) {

        sessionRepository
                .findByUserId(userId)
                .forEach(session -> {
                    session.setRevoked(true);
                    sessionRepository.save(session);
                });
    }

    public void touch(
            @NonNull Long sessionId) {

        Session session = validate(sessionId);

        session.setLastAccessedAt(
                Instant.now());

        sessionRepository.save(session);
    }
}