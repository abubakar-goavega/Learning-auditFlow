package com.abu.auditflow.auth.dto;

import java.time.Instant;

public record SecurityErrorResponse(

        Instant timestamp,

        int status,

        String error,

        String message,

        String path) {

    public SecurityErrorResponse(

            int status,

            String error,

            String message,

            String path) {

        this(

                Instant.now(),

                status,

                error,

                message,

                path);
    }
}