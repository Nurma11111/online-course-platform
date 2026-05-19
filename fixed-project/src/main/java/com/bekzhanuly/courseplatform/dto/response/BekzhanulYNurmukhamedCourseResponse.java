package com.bekzhanuly.courseplatform.dto.response;

import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedCourseStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Course Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedCourseResponse {
    private Long id;
    private String title;
    private String description;
    private String shortDescription;
    private String thumbnailUrl;
    private BigDecimal price;
    private BekzhanulYNurmukhamedCourseStatus status;
    private String language;
    private String difficultyLevel;
    private Integer durationHours;
    private Double rating;
    private Integer totalReviews;
    private Integer totalStudents;
    private Long instructorId;
    private String instructorName;
    private Long categoryId;
    private String categoryName;
    private Integer totalLessons;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
