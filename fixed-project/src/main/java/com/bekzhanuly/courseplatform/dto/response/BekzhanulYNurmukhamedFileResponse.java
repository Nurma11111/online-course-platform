package com.bekzhanuly.courseplatform.dto.response;

import lombok.*;
import java.time.LocalDateTime;

/**
 * File Attachment Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedFileResponse {
    private Long id;
    private String originalFilename;
    private String storedFilename;
    private Long fileSize;
    private String contentType;
    private Integer downloadCount;
    private Long lessonId;
    private String downloadUrl;
    private LocalDateTime uploadedAt;
}
