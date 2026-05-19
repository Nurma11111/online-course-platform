package com.bekzhanuly.courseplatform.dto.response;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Review Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime createdAt;
}
