package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedLessonRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedLessonResponse;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedLessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Lesson Controller
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/courses/{courseId}/lessons")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lessons", description = "Lesson management within courses")
public class BekzhanulYNurmukhamedLessonController {

    private final BekzhanulYNurmukhamedLessonService lessonService;

    @GetMapping
    @Operation(summary = "Get all lessons for a course")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<List<BekzhanulYNurmukhamedLessonResponse>>> getLessons(
            @PathVariable Long courseId) {
        log.info("GET /courses/{}/lessons", courseId);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                lessonService.getLessonsByCourse(courseId)));
    }

    @GetMapping("/{lessonId}")
    @Operation(summary = "Get a specific lesson by ID")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedLessonResponse>> getLessonById(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        log.info("GET /courses/{}/lessons/{}", courseId, lessonId);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                lessonService.getLessonById(courseId, lessonId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a lesson", description = "INSTRUCTOR or ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedLessonResponse>> createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody BekzhanulYNurmukhamedLessonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /courses/{}/lessons by {}", courseId, userDetails.getUsername());
        BekzhanulYNurmukhamedLessonResponse response =
                lessonService.createLesson(courseId, request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BekzhanulYNurmukhamedApiResponse.success(response, "Lesson created successfully"));
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a lesson")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedLessonResponse>> updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @Valid @RequestBody BekzhanulYNurmukhamedLessonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /courses/{}/lessons/{} by {}", courseId, lessonId, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                lessonService.updateLesson(courseId, lessonId, request, userDetails.getUsername()),
                "Lesson updated successfully"));
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a lesson")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /courses/{}/lessons/{} by {}", courseId, lessonId, userDetails.getUsername());
        lessonService.deleteLesson(courseId, lessonId, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(null, "Lesson deleted successfully"));
    }
}
