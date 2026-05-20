package com.bekzhanuly.courseplatform.dto.response;

import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedLessonType;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Lesson Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedLessonResponse {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private Integer durationMinutes;
    private Integer orderIndex;
    private BekzhanulYNurmukhamedLessonType lessonType;
    private Boolean isFreePreview;
    private Long courseId;
    private String courseTitle;
    private Long attachmentId;
    private String attachmentFilename;
    private LocalDateTime createdAt;
}
