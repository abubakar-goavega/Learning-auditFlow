package com.abu.auditflow.storage.util;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class FileKeyGenerator {

    public String generate(
            String originalFilename) {

        String extension = "";

        if (originalFilename != null
                && originalFilename.contains(".")) {

            extension = originalFilename.substring(
                    originalFilename.lastIndexOf('.'));
        }

        LocalDate now = LocalDate.now();

        return now.getYear()
                + "/"
                + String.format(
                        "%02d",
                        now.getMonthValue())
                + "/"
                + UUID.randomUUID()
                + extension;
    }
}