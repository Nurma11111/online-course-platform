package com.bekzhanuly.courseplatform.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Course Request DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedCourseRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Size(max = 500, message = "Short description max 500 characters")
    private String shortDescription;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    private Long categoryId;

    @Size(max = 50)
    private String language;

    @Size(max = 50)
    private String difficultyLevel;

    @Min(value = 1, message = "Duration must be at least 1 hour")
    private Integer durationHours;
}
