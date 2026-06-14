package com.abu.auditflow.auth.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abu.auditflow.auth.entity.Session;
import com.abu.auditflow.auth.security.userdetails.CustomUserDetails;
import com.abu.auditflow.auth.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/sessions")
public class SessionController {
    private final SessionService sessionService;

    @GetMapping("/sessions")
    public List<Session> getSessions(@AuthenticationPrincipal CustomUserDetails user) {
        return sessionService.getActiveSessions(user.getId());
    }

    @DeleteMapping("/sessions/{sessionId}")
    public void revokeSession(@PathVariable Long sessionId) {
        sessionService.revoke(sessionId);
    }

    @DeleteMapping("/sessions/all")
    public void revokeAll(@AuthenticationPrincipal CustomUserDetails user) {
        sessionService.revokeAll(user.getId());
    }
}
