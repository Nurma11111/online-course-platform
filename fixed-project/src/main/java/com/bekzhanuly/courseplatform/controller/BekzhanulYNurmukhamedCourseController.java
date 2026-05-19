package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedCourseRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCourseResponse;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedCourseStatus;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Course Controller - GET, POST, PUT, DELETE with Pagination, Search, Filter, Sort
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Courses", description = "Course management with full CRUD, search, pagination and filtering")
public class BekzhanulYNurmukhamedCourseController {

    private final BekzhanulYNurmukhamedCourseService courseService;

    /**
     * GET /courses — paginated, sorted, searchable, filterable list
     * Demonstrates: pagination, sorting, search, filtering with query params
     */
    @GetMapping
    @Operation(
        summary = "Get all courses",
        description = "Returns paginated, sorted, and filtered list of courses. " +
                      "Supports search by title/description, filter by status/category/price/language/difficulty."
    )
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Page<BekzhanulYNurmukhamedCourseResponse>>> getAllCourses(
            @Parameter(description = "Search by title or description")
            @RequestParam(required = false) String search,

            @Parameter(description = "Filter by status: DRAFT, PUBLISHED, ARCHIVED")
            @RequestParam(required = false) BekzhanulYNurmukhamedCourseStatus status,

            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) Long categoryId,

            @Parameter(description = "Filter by instructor ID")
            @RequestParam(required = false) Long instructorId,

            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Filter by language, e.g. English")
            @RequestParam(required = false) String language,

            @Parameter(description = "Filter by difficulty: BEGINNER, INTERMEDIATE, ADVANCED")
            @RequestParam(required = false) String difficulty,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field: title, price, rating, createdAt, totalStudents")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("GET /courses - search={}, status={}, page={}, size={}, sortBy={}", 
                search, status, page, size, sortBy);
        Page<BekzhanulYNurmukhamedCourseResponse> result = courseService.getAllCourses(
                search, status, categoryId, instructorId,
                minPrice, maxPrice, language, difficulty,
                page, size, sortBy, sortDir);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(result));
    }

    /**
     * GET /courses/top — top courses by enrollment count
     */
    @GetMapping("/top")
    @Operation(summary = "Get top courses by enrollment")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<List<BekzhanulYNurmukhamedCourseResponse>>> getTopCourses(
            @RequestParam(defaultValue = "5") int limit) {
        log.info("GET /courses/top - limit={}", limit);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                courseService.getTopCourses(limit)));
    }

    /**
     * GET /courses/{id} — get by path variable
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCourseResponse>> getCourseById(
            @PathVariable Long id) {
        log.info("GET /courses/{}", id);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                courseService.getCourseById(id)));
    }

    /**
     * POST /courses — create course (INSTRUCTOR or ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new course", description = "INSTRUCTOR or ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCourseResponse>> createCourse(
            @Valid @RequestBody BekzhanulYNurmukhamedCourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /courses by user: {}", userDetails.getUsername());
        BekzhanulYNurmukhamedCourseResponse response =
                courseService.createCourse(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BekzhanulYNurmukhamedApiResponse.success(response, "Course created successfully"));
    }

    /**
     * PUT /courses/{id} — update course
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update course", description = "Owner instructor or ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody BekzhanulYNurmukhamedCourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /courses/{} by user: {}", id, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                courseService.updateCourse(id, request, userDetails.getUsername()),
                "Course updated successfully"));
    }

    /**
     * PUT /courses/{id}/publish — publish a course
     */
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Publish a course")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedCourseResponse>> publishCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /courses/{}/publish by user: {}", id, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                courseService.publishCourse(id, userDetails.getUsername()),
                "Course published successfully"));
    }

    /**
     * DELETE /courses/{id} — delete course
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete course")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /courses/{} by user: {}", id, userDetails.getUsername());
        courseService.deleteCourse(id, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(null, "Course deleted successfully"));
    }
}
