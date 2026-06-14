package com.abu.auditflow.storage.provider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.abu.auditflow.config.storage.StorageProperties;

public class LocalStorageProvider implements StorageProvider {

        private final StorageProperties properties;

        public LocalStorageProvider(StorageProperties properties) {

                this.properties = properties;
        }

        @Override
        public String upload(MultipartFile file) {

                try {

                        String extension = getExtension(
                                        file.getOriginalFilename());

                        String fileName = UUID.randomUUID() + extension;

                        YearMonth now = YearMonth.now();

                        String folder = now.getYear() + "/" +
                                        String.format("%02d", now.getMonthValue());

                        Path uploadDir = Paths.get(
                                        properties.path(),
                                        folder);

                        Files.createDirectories(
                                        uploadDir);

                        Path targetFile = uploadDir.resolve(fileName);

                        Files.copy(
                                        file.getInputStream(),
                                        targetFile);

                        return folder + "/" + fileName;

                } catch (IOException ex) {
                        throw new RuntimeException("Failed to upload file", ex);
                }
        }

        @Override
        public Resource download(String storageKey) {

                try {

                        Path filePath = Paths.get(properties.path(), storageKey);

                        return new UrlResource(filePath.toUri());

                } catch (IOException ex) {
                        throw new RuntimeException("Failed to read file", ex);
                }
        }

        @Override
        public void delete(String storageKey) {

                try {

                        Path filePath = Paths.get(properties.path(), storageKey);

                        Files.deleteIfExists(filePath);

                } catch (IOException ex) {
                        throw new RuntimeException("Failed to delete file", ex);
                }
        }

        private String getExtension(String fileName) {

                if (fileName == null || !fileName.contains(".")) {
                        return "";
                }

                return fileName.substring(fileName.lastIndexOf("."));
        }

        @Override
        public String generateUploadUrl(String fileName, String contentType) {

                throw new UnsupportedOperationException("Presigned URLs not supported");
        }

        @Override
        public String generateDownloadUrl(String storageKey) {
                throw new UnsupportedOperationException("Presigned URLs not supported");
        }

        @Override
        public String providerName() {
                return "local";
        }

        @Override
        public boolean exists(
                        String storageKey) {

                Path filePath = Paths.get(properties.path(), storageKey);

                return Files.exists(filePath);
        }
}