package com.abu.auditflow.scheduler.cleanup;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.storage.entity.FileRecord;
import com.abu.auditflow.storage.entity.FileStatus;
import com.abu.auditflow.storage.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PendingUploadCleanupJob {

    private final FileRepository fileRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanup() {

        Instant cutoff =
                Instant.now()
                        .minus(1, ChronoUnit.HOURS);

        List<FileRecord> files =
                fileRepository
                        .findByStatusAndCreatedAtBefore(
                                FileStatus.PENDING,
                                cutoff);

        for (FileRecord file : files) {
            file.setStatus(
                    FileStatus.FAILED);
        }
    }
}