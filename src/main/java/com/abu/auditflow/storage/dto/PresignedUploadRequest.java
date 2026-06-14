package com.abu.auditflow.storage.dto;

public record PresignedUploadRequest(

        String fileName,

        String contentType,

        Long size

) {
}