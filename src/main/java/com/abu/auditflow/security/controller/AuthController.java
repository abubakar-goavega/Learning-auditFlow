package com.abu.auditflow.security.controller;

import com.abu.auditflow.common.dto.ApiResponse;
import com.abu.auditflow.security.dto.LoginRequest;
import com.abu.auditflow.security.dto.LoginResponse;
import com.abu.auditflow.security.dto.RefreshTokenRequest;
import com.abu.auditflow.security.dto.RefreshTokenResponse;
import com.abu.auditflow.security.dto.UserResponse;
import com.abu.auditflow.security.entity.RefreshToken;
import com.abu.auditflow.security.jwt.JwtService;
import com.abu.auditflow.security.service.RefreshTokenService;
import com.abu.auditflow.security.userdetails.CustomUserDetails;
import com.abu.auditflow.user.dto.UserDto;
import com.abu.auditflow.user.entity.User;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final RefreshTokenService refreshTokenService;

        public AuthController(
                        AuthenticationManager authenticationManager,
                        JwtService jwtService,
                        RefreshTokenService refreshTokenService) {

                this.authenticationManager = authenticationManager;
                this.jwtService = jwtService;
                this.refreshTokenService = refreshTokenService;
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/me")
        public ApiResponse<UserResponse> me(
                        Authentication authentication) {

                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

                return ApiResponse.success("User information retrieved", new UserResponse(
                                user.getId(),
                                user.getUsername(),
                                user.getRole(),
                                user.isEnabled()));
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/security-info")
        public ApiResponse<Map<String, Object>> info(
                        Authentication auth) {

                return ApiResponse.success("Security information retrieved", Map.of(
                                "user", auth.getName(),
                                "roles", auth.getAuthorities(),
                                "authenticated", auth.isAuthenticated()));
        }

        @PostMapping("/login")
        public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                Long userId = user.getId();
                String accessToken = jwtService.generateAccessToken(
                                userId,
                                user.getUsername(),
                                user.getRole());

                RefreshToken refreshToken = refreshTokenService.create(userId);

                return ApiResponse.success(new LoginResponse(
                                accessToken, refreshToken.getToken().toString(),
                                "Bearer",
                                jwtService.getAccessTokenExpirationMinutes() * 60,
                                new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.isEnabled())));
        }

        @PostMapping("/refresh")
        public ApiResponse<RefreshTokenResponse> refresh(
                        @Valid @RequestBody RefreshTokenRequest request) {

                RefreshToken refreshToken = refreshTokenService.validate(
                                request.refreshToken());

                UserDto user = refreshTokenService.getUser(refreshToken.getUserId());
                String accessToken = jwtService.generateAccessToken(
                                user.id(),user.username(),user.roleCode());
                return ApiResponse.success("Token refreshed",
                                new RefreshTokenResponse(
                                                user.id(),accessToken,"Bearer",900));
        }

        @PostMapping("/logout")
        public ApiResponse<Void> logout(
                        @Valid @RequestBody RefreshTokenRequest request) {
                refreshTokenService.revoke(request.refreshToken());
                return ApiResponse.success("Logged out", null);
        }

        @PostMapping("/logout-all")
        public ApiResponse<Void> logoutAll(
                        Authentication authentication) {
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                refreshTokenService.revokeAll(user.getId());
                return ApiResponse.success("Logged out from all devices", null);
        }
}