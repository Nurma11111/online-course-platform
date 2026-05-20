package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedCourseRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCourseResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCategory;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedCourseStatus;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedRole;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.mapper.BekzhanulYNurmukhamedCourseMapper;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCategoryRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCourseRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Course Service Implementation
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedCourseService {

    private final BekzhanulYNurmukhamedCourseRepository courseRepository;
    private final BekzhanulYNurmukhamedUserRepository userRepository;
    private final BekzhanulYNurmukhamedCategoryRepository categoryRepository;
    private final BekzhanulYNurmukhamedCourseMapper courseMapper;

    /**
     * GET all courses with pagination, sorting, search, filtering
     */
    @Transactional(readOnly = true)
    public Page<BekzhanulYNurmukhamedCourseResponse> getAllCourses(
            String search,
            BekzhanulYNurmukhamedCourseStatus status,
            Long categoryId,
            Long instructorId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String language,
            String difficulty,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BekzhanulYNurmukhamedCourse> coursePage = courseRepository.findAllWithFilters(
                search, status, categoryId, instructorId,
                minPrice, maxPrice, language, difficulty, pageable);

        log.debug("Fetched {} courses (page {}/{}) with filters", 
                coursePage.getNumberOfElements(), page, coursePage.getTotalPages());
        return coursePage.map(courseMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public BekzhanulYNurmukhamedCourseResponse getCourseById(Long id) {
        BekzhanulYNurmukhamedCourse course = findCourseOrThrow(id);
        return courseMapper.toResponse(course);
    }

    @Transactional
    public BekzhanulYNurmukhamedCourseResponse createCourse(
            BekzhanulYNurmukhamedCourseRequest request, String instructorUsername) {

        BekzhanulYNurmukhamedUser instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Instructor not found"));

        if (instructor.getRole() != BekzhanulYNurmukhamedRole.ROLE_INSTRUCTOR
                && instructor.getRole() != BekzhanulYNurmukhamedRole.ROLE_ADMIN) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "Only instructors can create courses");
        }

        BekzhanulYNurmukhamedCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                            .BekzhanulYNurmukhamedResourceNotFoundException("Category", request.getCategoryId()));
        }

        BekzhanulYNurmukhamedCourse course = BekzhanulYNurmukhamedCourse.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .price(request.getPrice())
                .language(request.getLanguage() != null ? request.getLanguage() : "English")
                .difficultyLevel(request.getDifficultyLevel())
                .durationHours(request.getDurationHours())
                .instructor(instructor)
                .category(category)
                .status(BekzhanulYNurmukhamedCourseStatus.DRAFT)
                .build();

        BekzhanulYNurmukhamedCourse saved = courseRepository.save(course);
        log.info("Course created: '{}' by instructor: {}", saved.getTitle(), instructorUsername);
        return courseMapper.toResponse(saved);
    }

    @Transactional
    public BekzhanulYNurmukhamedCourseResponse updateCourse(
            Long id, BekzhanulYNurmukhamedCourseRequest request, String username) {

        BekzhanulYNurmukhamedCourse course = findCourseOrThrow(id);
        verifyOwnershipOrAdmin(course, username);

        if (request.getCategoryId() != null) {
            BekzhanulYNurmukhamedCategory category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                            .BekzhanulYNurmukhamedResourceNotFoundException("Category", request.getCategoryId()));
            course.setCategory(category);
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setShortDescription(request.getShortDescription());
        course.setPrice(request.getPrice());
        if (request.getLanguage() != null) course.setLanguage(request.getLanguage());
        if (request.getDifficultyLevel() != null) course.setDifficultyLevel(request.getDifficultyLevel());
        if (request.getDurationHours() != null) course.setDurationHours(request.getDurationHours());

        BekzhanulYNurmukhamedCourse updated = courseRepository.save(course);
        log.info("Course updated: id={} by user: {}", id, username);
        return courseMapper.toResponse(updated);
    }

    @Transactional
    public BekzhanulYNurmukhamedCourseResponse publishCourse(Long id, String username) {
        BekzhanulYNurmukhamedCourse course = findCourseOrThrow(id);
        verifyOwnershipOrAdmin(course, username);

        if (course.getLessons().isEmpty()) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedBadRequestException(
                    "Cannot publish a course with no lessons");
        }

        course.setStatus(BekzhanulYNurmukhamedCourseStatus.PUBLISHED);
        BekzhanulYNurmukhamedCourse saved = courseRepository.save(course);
        log.info("Course published: id={}", id);
        return courseMapper.toResponse(saved);
    }

    @Transactional
    public void deleteCourse(Long id, String username) {
        BekzhanulYNurmukhamedCourse course = findCourseOrThrow(id);
        verifyOwnershipOrAdmin(course, username);
        courseRepository.delete(course);
        log.info("Course deleted: id={} by user: {}", id, username);
    }

    @Transactional(readOnly = true)
    public List<BekzhanulYNurmukhamedCourseResponse> getTopCourses(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return courseMapper.toResponseList(courseRepository.findTopCoursesByEnrollment(pageable));
    }

    private BekzhanulYNurmukhamedCourse findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Course", id));
    }

    private void verifyOwnershipOrAdmin(BekzhanulYNurmukhamedCourse course, String username) {
        BekzhanulYNurmukhamedUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));
        boolean isOwner = course.getInstructor().getUsername().equals(username);
        boolean isAdmin = user.getRole() == BekzhanulYNurmukhamedRole.ROLE_ADMIN;
        if (!isOwner && !isAdmin) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "You do not have permission to modify this course");
        }
    }
}
