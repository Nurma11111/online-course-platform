package com.bekzhanuly.courseplatform.controller;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedReviewRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedReviewResponse;
import com.bekzhanuly.courseplatform.service.impl.BekzhanulYNurmukhamedReviewService;
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

/**
 * Review Controller
 * Author: Bekzhanuly Nurmukhamed
 */
@RestController
@RequestMapping("/courses/{courseId}/reviews")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reviews", description = "Course review and rating management")
public class BekzhanulYNurmukhamedReviewController {

    private final BekzhanulYNurmukhamedReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get reviews for a course", description = "Paginated + sortable by date")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Page<BekzhanulYNurmukhamedReviewResponse>>> getCourseReviews(
            @PathVariable Long courseId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")  @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort direction: asc or desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /courses/{}/reviews - page={}, sortDir={}", courseId, page, sortDir);
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                reviewService.getCourseReviews(courseId, page, size, sortDir)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a review", description = "Must be enrolled in the course. Async rating recalculation triggered.")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedReviewResponse>> createReview(
            @PathVariable Long courseId,
            @Valid @RequestBody BekzhanulYNurmukhamedReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /courses/{}/reviews by {}", courseId, userDetails.getUsername());
        BekzhanulYNurmukhamedReviewResponse response =
                reviewService.createReview(courseId, request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BekzhanulYNurmukhamedApiResponse.success(response, "Review submitted successfully"));
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a review", description = "Only the review author can update")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<BekzhanulYNurmukhamedReviewResponse>> updateReview(
            @PathVariable Long courseId,
            @PathVariable Long reviewId,
            @Valid @RequestBody BekzhanulYNurmukhamedReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /courses/{}/reviews/{} by {}", courseId, reviewId, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(
                reviewService.updateReview(reviewId, request, userDetails.getUsername()),
                "Review updated"));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a review", description = "Author or ADMIN only")
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> deleteReview(
            @PathVariable Long courseId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /courses/{}/reviews/{} by {}", courseId, reviewId, userDetails.getUsername());
        reviewService.deleteReview(reviewId, userDetails.getUsername());
        return ResponseEntity.ok(BekzhanulYNurmukhamedApiResponse.success(null, "Review deleted"));
    }
}
