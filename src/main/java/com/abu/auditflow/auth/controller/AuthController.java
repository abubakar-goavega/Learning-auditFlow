package com.abu.auditflow.auth.controller;

import com.abu.auditflow.auth.dto.LoginRequest;
import com.abu.auditflow.auth.dto.LoginResponse;
import com.abu.auditflow.auth.dto.RefreshTokenRequest;
import com.abu.auditflow.auth.dto.RefreshTokenResponse;
import com.abu.auditflow.auth.dto.UserResponse;
import com.abu.auditflow.auth.security.userdetails.CustomUserDetails;
import com.abu.auditflow.auth.service.AuthService;
import com.abu.auditflow.common.dto.ApiResponse;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private AuthService authService;

        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/me")
        public ApiResponse<UserResponse> me(
                        Authentication authentication) {
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                return ApiResponse.success("User information retrieved", authService.me(user));
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
                return ApiResponse.success("Login successful", authService.login(request));
        }

        @PostMapping("/refresh")
        public ApiResponse<RefreshTokenResponse> refresh(
                        @Valid @RequestBody RefreshTokenRequest request) {

                return ApiResponse.success("Token refreshed", authService.refresh(request));
        }

        @PostMapping("/logout")
        public ApiResponse<Void> logout(
                        @Valid @RequestBody RefreshTokenRequest request) {
                authService.logout(request.refreshToken());
                return ApiResponse.success("Logged out", null);
        }

        @PostMapping("/logout-all")
        public ApiResponse<Void> logoutAll(
                        Authentication authentication) {
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                authService.logoutAll(user.getId());
                return ApiResponse.success("Logged out from all devices", null);
        }
}