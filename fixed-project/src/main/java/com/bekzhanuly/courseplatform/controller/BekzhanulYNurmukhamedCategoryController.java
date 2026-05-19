package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedCategoryRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCategoryResponse;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Controller
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Categories", description = "Course category management")
public class BekzhanulYNurmukhamedCategoryController {

    private final BekzhanulYNurmukhamedCategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all active categories")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<List<BekzhanulYNurmukhamedCategoryResponse>>> getAllCategories() {
        log.info("GET /categories");
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCategoryResponse>> getCategoryById(
            @PathVariable Long id) {
        log.info("GET /categories/{}", id);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(categoryService.getCategoryById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a category", description = "ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCategoryResponse>> createCategory(
            @Valid @RequestBody BekzhanulYNurmukhamedCategoryRequest request) {
        log.info("POST /categories - name: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BekzhanulYNurmukhamedApiResponse.success(
                        categoryService.createCategory(request), "Category created"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a category", description = "ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody BekzhanulYNurmukhamedCategoryRequest request) {
        log.info("PUT /categories/{}", id);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                categoryService.updateCategory(id, request), "Category updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Soft-delete a category", description = "ADMIN only — sets isActive=false")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /categories/{}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(null, "Category deactivated"));
    }
}
