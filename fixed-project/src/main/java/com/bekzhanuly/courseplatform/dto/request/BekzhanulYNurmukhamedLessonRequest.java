package com.bekzhanuly.courseplatform.dto.request;

import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedLessonType;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Lesson Request DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedLessonRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    private String content;

    private String videoUrl;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    private Integer orderIndex;

    @NotNull(message = "Lesson type is required")
    private BekzhanulYNurmukhamedLessonType lessonType;

    private Boolean isFreePreview;
}
