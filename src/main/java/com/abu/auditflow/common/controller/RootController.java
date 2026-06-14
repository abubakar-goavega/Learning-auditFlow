package com.abu.auditflow.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Root controller used for learning and testing.
 *
 * Demonstrates:
 * - Public endpoints
 * - Protected endpoints
 * - Authentication
 * - Authorization
 * - Method Security (@PreAuthorize)
 * - SecurityContext usage
 */
@RestController
public class RootController {

    /**
     * Public endpoint.
     *
     * No authentication required.
     *
     * Used to verify:
     * - Application is running
     * - Security allows anonymous access
     */
    @GetMapping("/")
    public Map<String, Object> home() {

        return Map.of(
                "application", "AuditFlow Template",
                "version", "1.0.0",
                "security", "enabled"
        );
    }


    /**
     * Public endpoint.
     *
     * Used for project information.
     */
    @GetMapping("/about")
    public Map<String, String> about() {

        return Map.of(
                "message",
                "Spring Boot Enterprise Template Learning Project"
        );
    }
    
}