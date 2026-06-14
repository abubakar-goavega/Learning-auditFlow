package com.abu.auditflow.config.storage;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
                String provider,
                String path,
                Long maxFileSizeMb,
                List<String> allowedContentTypes,
                String endpoint,
                String region,
                String accessKey,
                String secretKey,
                String bucket) {
}