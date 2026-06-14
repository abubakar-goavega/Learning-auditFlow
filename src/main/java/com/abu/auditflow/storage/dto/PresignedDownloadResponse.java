package com.abu.auditflow.storage.dto;

public record PresignedDownloadResponse(

        Long fileId,

        String downloadUrl

) {
}