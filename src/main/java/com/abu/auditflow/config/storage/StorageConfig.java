package com.abu.auditflow.config.storage;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.abu.auditflow.storage.provider.LocalStorageProvider;
import com.abu.auditflow.storage.provider.S3StorageProvider;
import com.abu.auditflow.storage.provider.StorageProvider;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {

    @Bean
    LocalStorageProvider localStorageProvider(
            StorageProperties properties) {

        return new LocalStorageProvider(properties);
    }

    @Bean
    S3StorageProvider s3StorageProvider(
            S3Client s3Client,
            StorageProperties properties,
            S3Presigner presigner) {

        return new S3StorageProvider(
                s3Client,
                properties,
                presigner);
    }

    @Bean
    StorageProvider storageProvider(
            StorageProperties properties,
            LocalStorageProvider local,
            S3StorageProvider s3) {

        return switch (properties.provider()
                .toLowerCase()) {

            case "s3" -> s3;

            default -> local;
        };
    }
}