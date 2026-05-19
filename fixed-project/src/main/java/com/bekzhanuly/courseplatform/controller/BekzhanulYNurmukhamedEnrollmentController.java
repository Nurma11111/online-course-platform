package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedEnrollmentResponse;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Enrollment Controller
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Enrollments", description = "Course enrollment management")
@SecurityRequirement(name = "bearerAuth")
public class BekzhanulYNurmukhamedEnrollmentController {

    private final BekzhanulYNurmukhamedEnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @Operation(summary = "Enroll in a course", description = "STUDENT only — triggers async email + stats update")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedEnrollmentResponse>> enroll(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /enrollments/courses/{} by {}", courseId, userDetails.getUsername());
        BekzhanulYNurmukhamedEnrollmentResponse response =
                enrollmentService.enrollStudent(courseId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BekzhanulYNurmukhamedApiResponse.success(response, "Enrolled successfully"));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my enrollments", description = "Returns paginated list of current user's enrollments")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Page<BekzhanulYNurmukhamedEnrollmentResponse>>> getMyEnrollments(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")  @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /enrollments/my - user: {}", userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                enrollmentService.getStudentEnrollments(userDetails.getUsername(), page, size)));
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Get enrollments for a course", description = "INSTRUCTOR (owner) or ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Page<BekzhanulYNurmukhamedEnrollmentResponse>>> getCourseEnrollments(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /enrollments/courses/{} by {}", courseId, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                enrollmentService.getCourseEnrollments(courseId, page, size, userDetails.getUsername())));
    }

    @PutMapping("/{enrollmentId}/progress")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @Operation(summary = "Update progress percentage", description = "Use query param ?progress=50")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedEnrollmentResponse>> updateProgress(
            @PathVariable Long enrollmentId,
            @RequestParam @Parameter(description = "Progress percentage 0–100") Integer progress,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /enrollments/{}/progress?progress={} by {}", enrollmentId, progress, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                enrollmentService.updateProgress(enrollmentId, progress, userDetails.getUsername()),
                "Progress updated"));
    }

    @DeleteMapping("/{enrollmentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @Operation(summary = "Drop (unenroll) from a course")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> dropEnrollment(
            @PathVariable Long enrollmentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /enrollments/{} by {}", enrollmentId, userDetails.getUsername());
        enrollmentService.dropEnrollment(enrollmentId, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(null, "Dropped from course"));
    }
}
