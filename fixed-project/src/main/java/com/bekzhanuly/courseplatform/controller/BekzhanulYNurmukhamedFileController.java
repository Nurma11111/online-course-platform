package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedFileResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedFileAttachment;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedFileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Controller — Upload and Download lesson attachments
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Files", description = "File upload and download for lesson attachments")
public class BekzhanulYNurmukhamedFileController {

    private final BekzhanulYNurmukhamedFileStorageService fileStorageService;

    /**
     * POST /files/upload?lessonId=1  — upload a file and attach to lesson
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Upload a file for a lesson",
        description = "Accepts PDF, images, video, zip, Word docs (max 50MB). " +
                      "Uses lessonId query param to attach the file to a lesson."
    )
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedFileResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lessonId") Long lessonId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /files/upload - lessonId={}, file={}, size={} bytes by {}",
                lessonId, file.getOriginalFilename(), file.getSize(), userDetails.getUsername());
        BekzhanulYNurmukhamedFileResponse response =
                fileStorageService.uploadFile(file, lessonId, userDetails.getUsername());
        return ResponseEntity.ok(
                BekzhanulYNurmukhamedApiResponse.success(response, "File uploaded successfully"));
    }

    /**
     * GET /files/{storedFilename}/download  — download a file by its stored name
     */
    @GetMapping("/{storedFilename}/download")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Download a file",
        description = "Returns the file as an octet-stream. Download counter is incremented asynchronously."
    )
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String storedFilename) {
        log.info("GET /files/{}/download", storedFilename);
        Resource resource = fileStorageService.downloadFile(storedFilename);

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    /**
     * GET /files/lessons/{lessonId}  — get file info for a lesson
     */
    @GetMapping("/lessons/{lessonId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get file attachment info for a lesson")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedFileResponse>> getFileByLesson(
            @PathVariable Long lessonId) {
        log.info("GET /files/lessons/{}", lessonId);
        BekzhanulYNurmukhamedFileAttachment attachment = fileStorageService.getAttachmentByLesson(lessonId);
        BekzhanulYNurmukhamedFileResponse response = BekzhanulYNurmukhamedFileResponse.builder()
                .id(attachment.getId())
                .originalFilename(attachment.getOriginalFilename())
                .storedFilename(attachment.getStoredFilename())
                .fileSize(attachment.getFileSize())
                .contentType(attachment.getContentType())
                .downloadCount(attachment.getDownloadCount())
                .lessonId(lessonId)
                .downloadUrl("/api/files/" + attachment.getStoredFilename() + "/download")
                .uploadedAt(attachment.getUploadedAt())
                .build();
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(response));
    }

    /**
     * DELETE /files/{fileId}  — delete a file
     */
    @DeleteMapping("/{fileId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a file", description = "INSTRUCTOR or ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> deleteFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /files/{} by {}", fileId, userDetails.getUsername());
        fileStorageService.deleteFile(fileId);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(null, "File deleted"));
    }
}
