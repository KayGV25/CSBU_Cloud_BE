package com.csbu.finance.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {
    @Value("${aws.s3.bucket.name:csbu-finance-storage}")
    private String bucketName;

    private final S3Client s3Client;

    public S3Service() {
        this.s3Client = S3Client.create();
    }

    public String upload(MultipartFile imgFile, String fileName) throws IOException {
        String fullPath = "software_design_storage/transactions/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fullPath)
                .contentType(imgFile.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(imgFile.getInputStream(), imgFile.getSize()));

        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucketName).key(fullPath))
                .toExternalForm();
    }
}
