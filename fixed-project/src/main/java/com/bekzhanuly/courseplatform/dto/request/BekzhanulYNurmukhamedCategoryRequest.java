package com.bekzhanuly.courseplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Category Request DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000)
    private String description;

    private String iconUrl;
}
