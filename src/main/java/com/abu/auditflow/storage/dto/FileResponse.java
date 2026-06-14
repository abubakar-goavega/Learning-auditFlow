package com.abu.auditflow.storage.dto;

import java.time.Instant;

public record FileResponse(

        Long id,

        String fileName,

        String originalFileName,

        String contentType,

        Long sizeBytes,

        String provider,

        Instant createdAt
) {
}