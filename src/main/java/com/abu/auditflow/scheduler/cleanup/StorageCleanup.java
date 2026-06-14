package com.abu.auditflow.scheduler.cleanup;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.abu.auditflow.storage.entity.FileRecord;
import com.abu.auditflow.storage.provider.StorageProvider;
import com.abu.auditflow.storage.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorageCleanup {

    private final FileRepository fileRepository;
    private final StorageProvider storageProvider;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanup() {

        Instant cutoff = Instant.now()
                .minus(7, ChronoUnit.DAYS);

        List<FileRecord> files = fileRepository
                .findByDeletedAtBefore(cutoff);

        for (FileRecord file : files) {

            storageProvider.delete(
                    file.getStorageKey());

            fileRepository.delete(file);
        }
    }
}