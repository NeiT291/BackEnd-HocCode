package com.neit.hoccode.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadImage(MultipartFile file) throws Exception {

        // 1. Validate
        if (file.isEmpty()) {
            throw new RuntimeException("File empty");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Only image allowed");
        }

        // 2. Tạo object name unique
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 3. Upload
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return objectName;
    }
}
