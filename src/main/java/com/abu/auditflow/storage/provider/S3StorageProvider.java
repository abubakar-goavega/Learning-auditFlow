package com.abu.auditflow.storage.provider;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.abu.auditflow.config.storage.StorageProperties;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.core.sync.RequestBody;

public class S3StorageProvider implements StorageProvider {

        private final S3Client s3Client;
        private final StorageProperties properties;
        private final S3Presigner presigner;

        public S3StorageProvider(S3Client s3Client, StorageProperties properties, S3Presigner presigner) {
                this.s3Client = s3Client;
                this.properties = properties;
                this.presigner = presigner;
        }

        private String buildStorageKey(
                        String originalFilename) {

                String extension = "";

                if (originalFilename != null
                                && originalFilename.contains(".")) {

                        extension = originalFilename.substring(
                                        originalFilename.lastIndexOf('.'));
                }

                LocalDate now = LocalDate.now();

                return now.getYear()
                                + "/"
                                + String.format("%02d", now.getMonthValue())
                                + "/"
                                + UUID.randomUUID()
                                + extension;
        }

        @Override
        public String upload(
                        MultipartFile file) {

                try {

                        String storageKey = buildStorageKey(
                                        file.getOriginalFilename());

                        PutObjectRequest request = PutObjectRequest.builder()
                                        .bucket(
                                                        properties.bucket())
                                        .key(storageKey)
                                        .contentType(
                                                        file.getContentType())
                                        .build();

                        s3Client.putObject(
                                        request,
                                        RequestBody.fromInputStream(
                                                        file.getInputStream(),
                                                        file.getSize()));

                        return storageKey;

                } catch (Exception ex) {

                        throw new RuntimeException(
                                        "Failed to upload file",
                                        ex);
                }
        }

        @Override
        public Resource download(String storageKey) {
                throw new UnsupportedOperationException("Use presigned URLs");
        }

        @Override
        public void delete(
                        String storageKey) {

                DeleteObjectRequest request = DeleteObjectRequest.builder()
                                .bucket(
                                                properties.bucket())
                                .key(storageKey)
                                .build();

                s3Client.deleteObject(
                                request);
        }

        @Override
        public String generateUploadUrl(String storageKey,String contentType) {

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .contentType(contentType)
                                .build();

                PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                                .signatureDuration(Duration.ofMinutes(15))
                                .putObjectRequest(putObjectRequest)
                                .build();

                return presigner
                                .presignPutObject(presignRequest)
                                .url()
                                .toString();
        }

        @Override
        public String generateDownloadUrl(
                        String storageKey) {

                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                .signatureDuration(
                                                Duration.ofMinutes(15))
                                .getObjectRequest(
                                                getObjectRequest)
                                .build();

                return presigner
                                .presignGetObject(
                                                presignRequest)
                                .url()
                                .toString();
        }

        @Override
        public String providerName() {
                return "S3";
        }

        @Override
        public boolean exists(String storageKey) {
                try {
                        s3Client.headObject(HeadObjectRequest.builder().bucket(properties.bucket())
                        .key(storageKey).build());
                        return true;
                } catch (Exception ex) {

                        return false;
                }
        }

}
