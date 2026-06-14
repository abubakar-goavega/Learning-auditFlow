package com.abu.auditflow.storage.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abu.auditflow.auth.security.userdetails.CustomUserDetails;
import com.abu.auditflow.common.dto.ApiResponse;
import com.abu.auditflow.storage.dto.FileResponse;
import com.abu.auditflow.storage.dto.PresignedDownloadResponse;
import com.abu.auditflow.storage.dto.PresignedUploadRequest;
import com.abu.auditflow.storage.dto.PresignedUploadResponse;
import com.abu.auditflow.storage.service.StorageService;

@RestController
@RequestMapping("/files")
public class StorageController {

        private final StorageService storageService;

        public StorageController(
                        StorageService storageService) {

                this.storageService = storageService;
        }

        @PostMapping
        public ApiResponse<FileResponse> upload(
                        @RequestParam("file") MultipartFile file,

                        Authentication authentication) {

                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

                return ApiResponse.success(
                                "File uploaded",
                                storageService.upload(
                                                file,
                                                user.getId()));
        }

        @GetMapping("/{id}")
        public ResponseEntity<Resource> download(@PathVariable Long id,Authentication authentication) {

                 CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                Resource resource = storageService.download(id, user);

                return ResponseEntity.ok()
                                .body(resource);
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> delete(
                        @PathVariable Long id) {

                storageService.delete(id);

                return ApiResponse.success(
                                "File deleted",
                                null);
        }

        @GetMapping("/{id}/metadata")
        public ApiResponse<FileResponse> metadata(
                        @PathVariable Long id) {

                return ApiResponse.success(
                                storageService.getMetadata(id));
        }

        @PostMapping("/{id}/restore")
        public ApiResponse<Void> restore(
                        @PathVariable Long id) {

                storageService.restore(id);

                return ApiResponse.success(
                                "File restored",
                                null);
        }

        @PostMapping("/upload-url")
        public ApiResponse<PresignedUploadResponse> uploadUrl(
                        @RequestBody PresignedUploadRequest request, Authentication authentication) {
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                return ApiResponse.success(
                                "Upload URL generated",
                                storageService.createUploadUrl(request, user.getId()));
        }

        @PostMapping("/{id}/complete")
        public ApiResponse<Void> completeUpload(
                        @PathVariable Long id) {

                storageService.completeUpload(id);

                return ApiResponse.success(
                                "Upload completed");
        }

        @GetMapping("/{id}/download-url")
        public ApiResponse<PresignedDownloadResponse> downloadUrl(
                        @PathVariable Long id) {

                return ApiResponse.success(
                                "Download URL generated",
                                storageService.getDownloadUrl(id));
        }
}