package com.abu.auditflow.storage.provider;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageProvider {

    String upload(MultipartFile file);

    Resource download(String storageKey);

    void delete(String storageKey);

    String generateUploadUrl(String fileName,String contentType);

    String generateDownloadUrl(String storageKey);

    String providerName();
    
    boolean exists(String storageKey);

}