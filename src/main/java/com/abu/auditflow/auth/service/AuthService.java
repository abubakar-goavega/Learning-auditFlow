package com.abu.auditflow.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.auth.dto.LoginRequest;
import com.abu.auditflow.auth.dto.LoginResponse;
import com.abu.auditflow.auth.dto.RefreshTokenRequest;
import com.abu.auditflow.auth.dto.RefreshTokenResponse;
import com.abu.auditflow.auth.dto.UserResponse;
import com.abu.auditflow.auth.entity.RefreshToken;
import com.abu.auditflow.auth.entity.Session;
import com.abu.auditflow.auth.security.jwt.JwtService;
import com.abu.auditflow.auth.security.userdetails.CustomUserDetails;
import com.abu.auditflow.common.util.IpUtils;
import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final SessionService sessionService;
    private final UserService userService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            SessionService sessionService,
            UserService userService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getId();
        String accessToken = jwtService.generateAccessToken(userId, user.getUsername(), user.getRole());

        String userAgent = httpRequest.getHeader("User-Agent");
        String clientIp = IpUtils.getClientIp(httpRequest);

        Session session = sessionService.create(userId, userAgent, clientIp);

        RefreshToken refreshToken = refreshTokenService.create(userId, session.getId());

        return new LoginResponse(
                accessToken, refreshToken.getToken().toString(),
                "Bearer",
                jwtService.getAccessTokenExpirationMinutes() * 60,
                new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.isEnabled()));
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {

        RefreshToken oldToken = refreshTokenService.validate(request.refreshToken());

        sessionService.validate(oldToken.getSessionId());
        sessionService.touch(oldToken.getSessionId());

        // REVOKE OLD TOKEN (THIS IS MISSING)
        refreshTokenService.revoke(request.refreshToken());

        // CREATE NEW REFRESH TOKEN
        RefreshToken newToken = refreshTokenService.create(oldToken.getUserId(), oldToken.getSessionId());

        UserDto user = userService.getUserById(oldToken.getUserId());

        String accessToken = jwtService.generateAccessToken(
                user.id(), user.username(), user.roleCode());

        return new RefreshTokenResponse(
                user.id(),accessToken,"Bearer",900,
                newToken.getToken().toString()
        );
    }

    public void logout(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.validate(refreshTokenStr);

        refreshTokenService.revoke(refreshTokenStr);

        sessionService.revoke(refreshToken.getSessionId());
    }

    public void logoutAll(long userId) {
        refreshTokenService.revokeAll(userId);
        sessionService.revokeAll(userId);
    }

    public UserResponse me(CustomUserDetails user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled());
    }
}