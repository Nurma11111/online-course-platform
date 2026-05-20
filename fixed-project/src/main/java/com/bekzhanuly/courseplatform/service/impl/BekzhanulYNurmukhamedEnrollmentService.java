package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedEnrollmentResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedEnrollment;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedCourseStatus;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedEnrollmentStatus;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.mapper.BekzhanulYNurmukhamedEnrollmentMapper;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCourseRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedEnrollmentRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import com.bekzhanuly.courseplatform.service.async.BekzhanulYNurmukhamedEmailNotificationService;
import com.bekzhanuly.courseplatform.service.async.BekzhanulYNurmukhamedStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Enrollment Service Implementation
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedEnrollmentService {

    private final BekzhanulYNurmukhamedEnrollmentRepository enrollmentRepository;
    private final BekzhanulYNurmukhamedCourseRepository courseRepository;
    private final BekzhanulYNurmukhamedUserRepository userRepository;
    private final BekzhanulYNurmukhamedEnrollmentMapper enrollmentMapper;
    private final BekzhanulYNurmukhamedEmailNotificationService emailService;
    private final BekzhanulYNurmukhamedStatisticsService statisticsService;

    @Transactional
    public BekzhanulYNurmukhamedEnrollmentResponse enrollStudent(Long courseId, String studentUsername) {
        BekzhanulYNurmukhamedUser student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));

        BekzhanulYNurmukhamedCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Course", courseId));

        if (course.getStatus() != BekzhanulYNurmukhamedCourseStatus.PUBLISHED) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedBadRequestException(
                    "Cannot enroll in an unpublished course");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException(
                    "Already enrolled in this course");
        }

        BekzhanulYNurmukhamedEnrollment enrollment = BekzhanulYNurmukhamedEnrollment.builder()
                .student(student)
                .course(course)
                .status(BekzhanulYNurmukhamedEnrollmentStatus.ACTIVE)
                .pricePaid(course.getPrice())
                .progressPercentage(0)
                .build();

        BekzhanulYNurmukhamedEnrollment saved = enrollmentRepository.save(enrollment);
        log.info("Student {} enrolled in course: {}", studentUsername, course.getTitle());

        // Async: send confirmation email
        emailService.sendEnrollmentConfirmationEmail(
                student.getEmail(), student.getFirstName(), course.getTitle());
        // Async: update course student count
        statisticsService.updateCourseStudentCount(courseId);

        return enrollmentMapper.toResponse(saved);
    }

    @Transactional
    public BekzhanulYNurmukhamedEnrollmentResponse updateProgress(
            Long enrollmentId, Integer progressPercentage, String studentUsername) {

        BekzhanulYNurmukhamedEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Enrollment", enrollmentId));

        if (!enrollment.getStudent().getUsername().equals(studentUsername)) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "You can only update your own progress");
        }

        if (progressPercentage < 0 || progressPercentage > 100) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedBadRequestException(
                    "Progress must be between 0 and 100");
        }

        enrollment.setProgressPercentage(progressPercentage);

        if (progressPercentage == 100
                && enrollment.getStatus() == BekzhanulYNurmukhamedEnrollmentStatus.ACTIVE) {
            enrollment.setStatus(BekzhanulYNurmukhamedEnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
            // Async: send completion email
            emailService.sendCourseCompletionEmail(
                    enrollment.getStudent().getEmail(),
                    enrollment.getStudent().getFirstName(),
                    enrollment.getCourse().getTitle());
            log.info("Student {} completed course: {}",
                    studentUsername, enrollment.getCourse().getTitle());
        }

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public Page<BekzhanulYNurmukhamedEnrollmentResponse> getStudentEnrollments(
            String studentUsername, int page, int size) {
        BekzhanulYNurmukhamedUser student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("enrolledAt").descending());
        return enrollmentRepository.findByStudentId(student.getId(), pageable)
                .map(enrollmentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BekzhanulYNurmukhamedEnrollmentResponse> getCourseEnrollments(
            Long courseId, int page, int size, String instructorUsername) {

        BekzhanulYNurmukhamedCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Course", courseId));

        // Only course instructor or admin can view
        BekzhanulYNurmukhamedUser caller = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));

        boolean isOwner = course.getInstructor().getUsername().equals(instructorUsername);
        boolean isAdmin = caller.getRole().name().equals("ROLE_ADMIN");
        if (!isOwner && !isAdmin) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "Access denied");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("enrolledAt").descending());
        return enrollmentRepository.findByCourseId(courseId, pageable)
                .map(enrollmentMapper::toResponse);
    }

    @Transactional
    public void dropEnrollment(Long enrollmentId, String studentUsername) {
        BekzhanulYNurmukhamedEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Enrollment", enrollmentId));

        if (!enrollment.getStudent().getUsername().equals(studentUsername)) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "You can only drop your own enrollment");
        }

        enrollment.setStatus(BekzhanulYNurmukhamedEnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
        statisticsService.updateCourseStudentCount(enrollment.getCourse().getId());
        log.info("Student {} dropped from course: {}", studentUsername, enrollment.getCourse().getTitle());
    }
}
