package com.example.ecs_cicd.service;

import com.example.ecs_cicd.config.S3Config;
import com.example.ecs_cicd.dto.PhotoUploadRequest;
import com.example.ecs_cicd.model.Photo;
import com.example.ecs_cicd.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final S3Config s3Config;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final PhotoRepository photoRepository;

    public void uploadPhoto(MultipartFile file, PhotoUploadRequest request) throws Exception {
        String objectKey = "photos/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Upload file
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(s3Config.getBucket())
                        .key(objectKey)
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        // Generate presigned URL
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(3))
                .getObjectRequest(getObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignGetObject(presignRequest).url().toString();

        // Save metadata
        Photo photo = Photo.builder()
                .objectKey(objectKey)
                .description(request.getDescription())
                .presignedUrl(presignedUrl)
                .build();

        photoRepository.save(photo);
    }
}
