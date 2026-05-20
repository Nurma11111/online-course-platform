package com.bekzhanuly.courseplatform.dto.response;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Category Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private Boolean isActive;
    private Integer totalCourses;
    private LocalDateTime createdAt;
}
