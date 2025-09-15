package com.example.EECToronto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class S3Service {
    private S3Client s3Client;

    @Value("${DO_SPACES_BUCKET}")
    private String bucketName;

    @Value("${DO_SPACES_ENDPOINT}")
    private String endpoint;

    @Value("${DO_SPACES_REGION}")
    private String region;

    public S3Service(@Value("${DO_SPACES_ACCESS_KEY}") String accessKey,
                     @Value("${DO_SPACES_SECRET_KEY}") String secretKey,
                     @Value("${DO_SPACES_BUCKET}") String bucketName,
                     @Value("${DO_SPACES_ENDPOINT}") String endpoint,
                     @Value("${DO_SPACES_REGION}") String region) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .endpointOverride(java.net.URI.create(endpoint))
                .build();
    }

    public String uploadFile(String fileName, byte[] data) throws IOException {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl("public-read")
                .build();
        s3Client.putObject(putRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(data));
        return endpoint + "/" + bucketName + "/" + fileName;
    }

}
