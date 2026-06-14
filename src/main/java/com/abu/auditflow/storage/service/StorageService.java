package com.abu.auditflow.storage.service;

import java.time.Instant;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abu.auditflow.auth.security.userdetails.CustomUserDetails;
import com.abu.auditflow.config.storage.StorageProperties;
import com.abu.auditflow.storage.dto.FileResponse;
import com.abu.auditflow.storage.dto.PresignedDownloadResponse;
import com.abu.auditflow.storage.dto.PresignedUploadRequest;
import com.abu.auditflow.storage.dto.PresignedUploadResponse;
import com.abu.auditflow.storage.entity.FileRecord;
import com.abu.auditflow.storage.entity.FileStatus;
import com.abu.auditflow.storage.mapper.FileMapper;
import com.abu.auditflow.storage.provider.StorageProvider;
import com.abu.auditflow.storage.repository.FileRepository;
import com.abu.auditflow.storage.util.FileKeyGenerator;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageService {

        private final FileRepository fileRepository;
        private final StorageProvider storageProvider;
        private final FileMapper fileMapper;
        private final StorageProperties properties;
        private final FileKeyGenerator fileKeyGenerator;

        public StorageService(
                        FileRepository fileRepository, StorageProvider storageProvider,
                        FileMapper fileMapper, StorageProperties properties, FileKeyGenerator fileKeyGenerator) {

                this.fileRepository = fileRepository;
                this.storageProvider = storageProvider;
                this.fileMapper = fileMapper;
                this.properties = properties;
                this.fileKeyGenerator = fileKeyGenerator;
        }

        private void validate(MultipartFile file) {

                if (file.isEmpty()) {
                        throw new IllegalArgumentException("File is empty");
                }

                long maxBytes = properties.maxFileSizeMb() * 1024 * 1024;

                if (file.getSize() > maxBytes) {
                        throw new IllegalArgumentException("File size exceeds limit");
                }

                if (!properties.allowedContentTypes()
                                .contains(file.getContentType())) {
                        throw new IllegalArgumentException("Unsupported file type");
                }
        }

        private void validateOwnership(FileRecord file,CustomUserDetails user) {

                boolean isOwner = Objects.equals(file.getUploadedBy(),user.getId());
                boolean isAdmin = user.getRole().equals("ROLE_ADMIN");

                if (!isOwner && !isAdmin) {
                        throw new IllegalArgumentException("You do not own this file");
                }
        }

        public FileResponse upload(MultipartFile file, Long uploadedBy) {

                validate(file);

                String storageKey = storageProvider.upload(file);

                FileRecord record = new FileRecord();
                record.setFileName(extractFileName(storageKey));
                record.setOriginalFileName(file.getOriginalFilename());
                record.setContentType(file.getContentType());
                record.setSizeBytes(file.getSize());
                record.setProvider(storageProvider.providerName());
                record.setStorageKey(storageKey);
                record.setUploadedBy(uploadedBy);
                record.setStatus(FileStatus.ACTIVE);
                FileRecord saved = fileRepository.save(record);

                return fileMapper.toResponse(saved);
        }

        @Transactional(readOnly = true)
        public Resource download(Long fileId,  CustomUserDetails user) {
                FileRecord file = fileRepository.findByIdAndDeletedAtIsNull(fileId).orElseThrow();
                validateOwnership(file, user);
                return storageProvider.download(file.getStorageKey());
        }

        @Transactional(readOnly = true)
        public FileResponse getMetadata(Long id) {
                FileRecord file = fileRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
                return fileMapper.toResponse(file);
        }

        public void restore(Long fileId) {
                FileRecord file = fileRepository.findById(fileId).orElseThrow();
                file.setDeletedAt(null);
        }

        public void delete(Long fileId) {

                FileRecord file = fileRepository
                                .findByIdAndDeletedAtIsNull(fileId)
                                .orElseThrow();

                file.setDeletedAt(Instant.now());
                fileRepository.save(file);
        }

        private String extractFileName(String storageKey) {
                int index = storageKey.lastIndexOf("/");
                return storageKey.substring(index + 1);
        }

        public PresignedUploadResponse createUploadUrl(
                        PresignedUploadRequest request, Long uploadedBy) {

                String storageKey = fileKeyGenerator.generate(request.fileName());

                FileRecord file = new FileRecord();
                file.setOriginalFileName(request.fileName());
                file.setFileName(request.fileName());
                file.setContentType(request.contentType());
                file.setSizeBytes(request.size());
                file.setStorageKey(storageKey);
                file.setProvider(storageProvider.providerName());
                file.setStatus(FileStatus.PENDING);
                file.setUploadedBy(uploadedBy);

                String uploadUrl = storageProvider.generateUploadUrl(storageKey, request.contentType());
                FileRecord savedFile = fileRepository.save(file);

                return new PresignedUploadResponse(savedFile.getId(), storageKey, uploadUrl);
        }

        public void completeUpload(Long fileId) {
                FileRecord file = fileRepository.findById(fileId)
                                .orElseThrow();
                if (file.getStatus() != FileStatus.PENDING) {
                        throw new IllegalStateException(
                                        "Upload already completed");
                }
                boolean exists = storageProvider.exists(
                                file.getStorageKey());

                if (!exists) {

                        throw new IllegalStateException(
                                        "File not uploaded");
                }
                file.setStatus(
                                FileStatus.ACTIVE);

                fileRepository.save(file);
        }

        @Transactional(readOnly = true)
        public PresignedDownloadResponse getDownloadUrl(
                        Long fileId) {
                FileRecord file = fileRepository.findById(fileId)
                                .orElseThrow();
                if (file.getStatus() != FileStatus.ACTIVE) {

                        throw new IllegalStateException(
                                        "File not available");
                }
                String url = storageProvider.generateDownloadUrl(
                                file.getStorageKey());
                return new PresignedDownloadResponse(
                                file.getId(),
                                url);
        }
}