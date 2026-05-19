package com.bekzhanuly.courseplatform.service.async;

import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCourseRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedEnrollmentRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * Async Statistics Recalculation Service
 * Async Process #2 - Statistics updates via @Async + @Scheduled
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedStatisticsService {

    private final BekzhanulYNurmukhamedCourseRepository courseRepository;
    private final BekzhanulYNurmukhamedReviewRepository reviewRepository;
    private final BekzhanulYNurmukhamedEnrollmentRepository enrollmentRepository;

    /**
     * Async: recalculate rating for a single course after new review
     */
    @Async("bekzhanulYAsyncExecutor")
    @Transactional
    public CompletableFuture<Void> recalculateCourseRating(Long courseId) {
        log.info("[ASYNC-STATS] Recalculating rating for course id: {}", courseId);
        return CompletableFuture.runAsync(() -> {
            try {
                courseRepository.findById(courseId).ifPresent(course -> {
                    Double avgRating = reviewRepository.findAverageRatingByCourseId(courseId);
                    long totalReviews = reviewRepository.countByCourseId(courseId);
                    course.setRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
                    course.setTotalReviews((int) totalReviews);
                    courseRepository.save(course);
                    log.info("[ASYNC-STATS] Course {} rating updated to {}, reviews: {}",
                            courseId, course.getRating(), totalReviews);
                });
            } catch (Exception e) {
                log.error("[ASYNC-STATS] Error recalculating rating for course {}: {}", courseId, e.getMessage());
            }
        });
    }

    /**
     * Async: update student count after enrollment
     */
    @Async("bekzhanulYAsyncExecutor")
    @Transactional
    public CompletableFuture<Void> updateCourseStudentCount(Long courseId) {
        log.info("[ASYNC-STATS] Updating student count for course id: {}", courseId);
        return CompletableFuture.runAsync(() -> {
            try {
                courseRepository.findById(courseId).ifPresent(course -> {
                    long activeStudents = enrollmentRepository
                            .countByCourseIdAndStatus(courseId,
                                    com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedEnrollmentStatus.ACTIVE);
                    course.setTotalStudents((int) activeStudents);
                    courseRepository.save(course);
                    log.info("[ASYNC-STATS] Course {} student count updated to {}", courseId, activeStudents);
                });
            } catch (Exception e) {
                log.error("[ASYNC-STATS] Error updating student count for course {}: {}", courseId, e.getMessage());
            }
        });
    }

    /**
     * Async Process #3: Scheduled full statistics recalculation every night
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Async("bekzhanulYAsyncExecutor")
    @Transactional
    public void scheduledFullStatisticsRecalculation() {
        log.info("[ASYNC-SCHEDULED] Starting full statistics recalculation...");
        try {
            courseRepository.findAll().forEach(course -> {
                Double avgRating = reviewRepository.findAverageRatingByCourseId(course.getId());
                long totalReviews = reviewRepository.countByCourseId(course.getId());
                long totalStudents = enrollmentRepository.countByCourseIdAndStatus(
                        course.getId(),
                        com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedEnrollmentStatus.ACTIVE);
                course.setRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
                course.setTotalReviews((int) totalReviews);
                course.setTotalStudents((int) totalStudents);
                courseRepository.save(course);
            });
            log.info("[ASYNC-SCHEDULED] Full statistics recalculation completed.");
        } catch (Exception e) {
            log.error("[ASYNC-SCHEDULED] Error during full statistics recalculation: {}", e.getMessage(), e);
        }
    }
}
