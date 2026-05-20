package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedReviewRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedReviewResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedReview;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.mapper.BekzhanulYNurmukhamedReviewMapper;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCourseRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedEnrollmentRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedReviewRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import com.bekzhanuly.courseplatform.service.async.BekzhanulYNurmukhamedEmailNotificationService;
import com.bekzhanuly.courseplatform.service.async.BekzhanulYNurmukhamedStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Review Service Implementation
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedReviewService {

    private final BekzhanulYNurmukhamedReviewRepository reviewRepository;
    private final BekzhanulYNurmukhamedCourseRepository courseRepository;
    private final BekzhanulYNurmukhamedUserRepository userRepository;
    private final BekzhanulYNurmukhamedEnrollmentRepository enrollmentRepository;
    private final BekzhanulYNurmukhamedReviewMapper reviewMapper;
    private final BekzhanulYNurmukhamedStatisticsService statisticsService;
    private final BekzhanulYNurmukhamedEmailNotificationService emailService;

    @Transactional
    public BekzhanulYNurmukhamedReviewResponse createReview(
            Long courseId, BekzhanulYNurmukhamedReviewRequest request, String username) {

        BekzhanulYNurmukhamedUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));

        BekzhanulYNurmukhamedCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Course", courseId));

        if (!enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), courseId)) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedBadRequestException(
                    "You must be enrolled in the course to leave a review");
        }

        if (reviewRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException(
                    "You have already reviewed this course");
        }

        BekzhanulYNurmukhamedReview review = BekzhanulYNurmukhamedReview.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .user(user)
                .course(course)
                .build();

        BekzhanulYNurmukhamedReview saved = reviewRepository.save(review);
        log.info("Review created by {} for course id={}", username, courseId);

        // Async: recalculate course rating
        statisticsService.recalculateCourseRating(courseId);
        // Async: notify instructor
        emailService.notifyInstructorAboutReview(
                course.getInstructor().getEmail(), course.getTitle(), request.getRating());

        return reviewMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<BekzhanulYNurmukhamedReviewResponse> getCourseReviews(
            Long courseId, int page, int size, String sortDir) {

        if (!courseRepository.existsById(courseId)) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedResourceNotFoundException("Course", courseId);
        }
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewRepository.findByCourseId(courseId, pageable)
                .map(reviewMapper::toResponse);
    }

    @Transactional
    public BekzhanulYNurmukhamedReviewResponse updateReview(
            Long reviewId, BekzhanulYNurmukhamedReviewRequest request, String username) {

        BekzhanulYNurmukhamedReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Review", reviewId));

        if (!review.getUser().getUsername().equals(username)) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "You can only edit your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        BekzhanulYNurmukhamedReview updated = reviewRepository.save(review);

        // Async: recalculate rating
        statisticsService.recalculateCourseRating(review.getCourse().getId());
        log.info("Review {} updated by {}", reviewId, username);
        return reviewMapper.toResponse(updated);
    }

    @Transactional
    public void deleteReview(Long reviewId, String username) {
        BekzhanulYNurmukhamedReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Review", reviewId));

        BekzhanulYNurmukhamedUser caller = userRepository.findByUsername(username)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));

        boolean isOwner = review.getUser().getUsername().equals(username);
        boolean isAdmin = caller.getRole().name().equals("ROLE_ADMIN");

        if (!isOwner && !isAdmin) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "You cannot delete this review");
        }

        Long courseId = review.getCourse().getId();
        reviewRepository.delete(review);
        statisticsService.recalculateCourseRating(courseId);
        log.info("Review {} deleted by {}", reviewId, username);
    }
}
