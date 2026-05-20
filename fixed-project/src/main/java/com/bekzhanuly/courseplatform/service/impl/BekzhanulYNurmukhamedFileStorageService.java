package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedFileResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedFileAttachment;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedLesson;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedFileAttachmentRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedLessonRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * File Storage Service - Upload and Download
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedFileStorageService {

    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "application/pdf", "image/jpeg", "image/png", "image/gif",
            "video/mp4", "video/avi", "application/zip",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
    );

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    private Path uploadPath;

    private final BekzhanulYNurmukhamedFileAttachmentRepository fileRepository;
    private final BekzhanulYNurmukhamedLessonRepository lessonRepository;
    private final BekzhanulYNurmukhamedUserRepository userRepository;

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
            log.info("File upload directory created at: {}", uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadPath, e);
        }
    }

    @Transactional
    public BekzhanulYNurmukhamedFileResponse uploadFile(
            MultipartFile file, Long lessonId, String uploaderUsername) {

        validateFile(file);

        BekzhanulYNurmukhamedLesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Lesson", lessonId));

        BekzhanulYNurmukhamedUser uploader = userRepository.findByUsername(uploaderUsername)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found: " + uploaderUsername));

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getFileExtension(originalFilename);
        String storedFilename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

        try {
            Path targetLocation = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File uploaded: {} -> {}", originalFilename, storedFilename);

            BekzhanulYNurmukhamedFileAttachment attachment = BekzhanulYNurmukhamedFileAttachment.builder()
                    .originalFilename(originalFilename)
                    .storedFilename(storedFilename)
                    .filePath(targetLocation.toString())
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .lesson(lesson)
                    .uploadedBy(uploader)
                    .build();

            BekzhanulYNurmukhamedFileAttachment saved = fileRepository.save(attachment);

            return BekzhanulYNurmukhamedFileResponse.builder()
                    .id(saved.getId())
                    .originalFilename(saved.getOriginalFilename())
                    .storedFilename(saved.getStoredFilename())
                    .fileSize(saved.getFileSize())
                    .contentType(saved.getContentType())
                    .downloadCount(saved.getDownloadCount())
                    .lessonId(lessonId)
                    .downloadUrl("/files/" + storedFilename + "/download")
                    .uploadedAt(saved.getUploadedAt())
                    .build();

        } catch (IOException e) {
            log.error("Failed to store file {}: {}", originalFilename, e.getMessage());
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedFileStorageException("Failed to store file: " + originalFilename, e);
        }
    }

    @Transactional
    public Resource downloadFile(String storedFilename) {
        BekzhanulYNurmukhamedFileAttachment attachment = fileRepository.findByStoredFilename(storedFilename)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("File not found: " + storedFilename));

        try {
            Path filePath = uploadPath.resolve(storedFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedFileStorageException("File not readable: " + storedFilename);
            }

            // Increment download counter
            attachment.setDownloadCount(attachment.getDownloadCount() + 1);
            fileRepository.save(attachment);
            log.info("File downloaded: {}, total downloads: {}", storedFilename, attachment.getDownloadCount());

            return resource;
        } catch (MalformedURLException e) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedFileStorageException("Malformed file path: " + storedFilename, e);
        }
    }

    @Transactional
    public void deleteFile(Long fileId) {
        BekzhanulYNurmukhamedFileAttachment attachment = fileRepository.findById(fileId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("File", fileId));

        try {
            Path filePath = uploadPath.resolve(attachment.getStoredFilename()).normalize();
            Files.deleteIfExists(filePath);
            fileRepository.delete(attachment);
            log.info("File deleted: {}", attachment.getStoredFilename());
        } catch (IOException e) {
            log.error("Could not delete file {}: {}", attachment.getStoredFilename(), e.getMessage());
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedFileStorageException("Could not delete file", e);
        }
    }

    public BekzhanulYNurmukhamedFileAttachment getAttachmentByLesson(Long lessonId) {
        return fileRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("No file found for lesson: " + lessonId));
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedBadRequestException("Cannot upload empty file");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedBadRequestException(
                    "File type not allowed: " + file.getContentType());
        }
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedBadRequestException("File size exceeds 50MB limit");
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex + 1) : "";
    }
}
