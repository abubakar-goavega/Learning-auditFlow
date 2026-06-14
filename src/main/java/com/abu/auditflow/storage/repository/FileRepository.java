package com.abu.auditflow.storage.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abu.auditflow.storage.entity.FileRecord;
import com.abu.auditflow.storage.entity.FileStatus;

public interface FileRepository
        extends JpaRepository<FileRecord, Long> {

    Optional<FileRecord> findByIdAndDeletedAtIsNull(Long id);

    List<FileRecord> findByDeletedAtBefore(Instant cutoff);

    List<FileRecord> findByStatusAndCreatedAtBefore(FileStatus status, Instant createdAt);

    Optional<FileRecord> findByIdAndDeletedAtIsNullAndStatus(Long id,
            FileStatus status);
}