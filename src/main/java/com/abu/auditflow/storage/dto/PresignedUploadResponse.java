package com.abu.auditflow.storage.dto;

public record PresignedUploadResponse(

        Long fileId,

        String storageKey,

        String uploadUrl

) {
}