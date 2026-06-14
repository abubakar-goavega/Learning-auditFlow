package com.abu.auditflow.storage.initializer;

import org.springframework.stereotype.Component;

import com.abu.auditflow.config.storage.StorageProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Component
public class S3BucketInitializer {
    private final S3Client s3Client;
    private final StorageProperties properties;

    public S3BucketInitializer(
            S3Client s3Client,
            StorageProperties properties) {

        this.s3Client = s3Client;
        this.properties = properties;

        initializeBucket();
    }

    @PostConstruct
    public void initializeBucket() {

        String bucket = properties.bucket();

        boolean exists = s3Client.listBuckets()
                .buckets()
                .stream()
                .anyMatch(
                        b -> b.name()
                                .equals(bucket));

        if (!exists) {

            s3Client.createBucket(
                    CreateBucketRequest.builder()
                            .bucket(bucket)
                            .build());
        }
    }
}
