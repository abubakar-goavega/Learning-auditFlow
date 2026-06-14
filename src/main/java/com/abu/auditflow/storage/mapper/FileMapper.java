package com.abu.auditflow.storage.mapper;

import org.springframework.stereotype.Component;

import com.abu.auditflow.storage.dto.FileResponse;
import com.abu.auditflow.storage.entity.FileRecord;

@Component
public class FileMapper {

    public FileResponse toResponse(
            FileRecord file) {

        return new FileResponse(
                file.getId(),
                file.getFileName(),
                file.getOriginalFileName(),
                file.getContentType(),
                file.getSizeBytes(),
                file.getProvider(),
                file.getCreatedAt());
    }
}