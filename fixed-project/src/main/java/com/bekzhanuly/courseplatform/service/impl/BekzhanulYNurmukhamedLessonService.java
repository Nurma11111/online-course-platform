package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedLessonRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedLessonResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedLesson;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedRole;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.mapper.BekzhanulYNurmukhamedLessonMapper;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCourseRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedLessonRepository;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lesson Service Implementation
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedLessonService {

    private final BekzhanulYNurmukhamedLessonRepository lessonRepository;
    private final BekzhanulYNurmukhamedCourseRepository courseRepository;
    private final BekzhanulYNurmukhamedUserRepository userRepository;
    private final BekzhanulYNurmukhamedLessonMapper lessonMapper;

    @Transactional(readOnly = true)
    public List<BekzhanulYNurmukhamedLessonResponse> getLessonsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedResourceNotFoundException("Course", courseId);
        }
        return lessonMapper.toResponseList(
                lessonRepository.findByCourseIdOrderByOrderIndex(courseId));
    }

    @Transactional(readOnly = true)
    public BekzhanulYNurmukhamedLessonResponse getLessonById(Long courseId, Long lessonId) {
        BekzhanulYNurmukhamedLesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Lesson", lessonId));
        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedBadRequestException("Lesson does not belong to this course");
        }
        return lessonMapper.toResponse(lesson);
    }

    @Transactional
    public BekzhanulYNurmukhamedLessonResponse createLesson(
            Long courseId, BekzhanulYNurmukhamedLessonRequest request, String username) {

        BekzhanulYNurmukhamedCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Course", courseId));

        verifyInstructorOwnership(course, username);

        Integer maxOrder = lessonRepository.findMaxOrderIndexByCourseId(courseId);
        int orderIndex = request.getOrderIndex() != null
                ? request.getOrderIndex()
                : (maxOrder != null ? maxOrder + 1 : 1);

        BekzhanulYNurmukhamedLesson lesson = BekzhanulYNurmukhamedLesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .videoUrl(request.getVideoUrl())
                .durationMinutes(request.getDurationMinutes())
                .orderIndex(orderIndex)
                .lessonType(request.getLessonType())
                .isFreePreview(request.getIsFreePreview() != null ? request.getIsFreePreview() : false)
                .course(course)
                .build();

        BekzhanulYNurmukhamedLesson saved = lessonRepository.save(lesson);
        log.info("Lesson '{}' created for course id={} by {}", saved.getTitle(), courseId, username);
        return lessonMapper.toResponse(saved);
    }

    @Transactional
    public BekzhanulYNurmukhamedLessonResponse updateLesson(
            Long courseId, Long lessonId,
            BekzhanulYNurmukhamedLessonRequest request, String username) {

        BekzhanulYNurmukhamedLesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Lesson", lessonId));

        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedBadRequestException("Lesson does not belong to this course");
        }

        verifyInstructorOwnership(lesson.getCourse(), username);

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setDurationMinutes(request.getDurationMinutes());
        lesson.setLessonType(request.getLessonType());
        if (request.getOrderIndex() != null) lesson.setOrderIndex(request.getOrderIndex());
        if (request.getIsFreePreview() != null) lesson.setIsFreePreview(request.getIsFreePreview());

        BekzhanulYNurmukhamedLesson updated = lessonRepository.save(lesson);
        log.info("Lesson {} updated by {}", lessonId, username);
        return lessonMapper.toResponse(updated);
    }

    @Transactional
    public void deleteLesson(Long courseId, Long lessonId, String username) {
        BekzhanulYNurmukhamedLesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Lesson", lessonId));

        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new BekzhanulYNurmukhamedExceptions
                    .BekzhanulYNurmukhamedBadRequestException("Lesson does not belong to this course");
        }

        verifyInstructorOwnership(lesson.getCourse(), username);
        lessonRepository.delete(lesson);
        log.info("Lesson {} deleted from course {} by {}", lessonId, courseId, username);
    }

    private void verifyInstructorOwnership(BekzhanulYNurmukhamedCourse course, String username) {
        BekzhanulYNurmukhamedUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("User not found"));
        boolean isOwner = course.getInstructor().getUsername().equals(username);
        boolean isAdmin = user.getRole() == BekzhanulYNurmukhamedRole.ROLE_ADMIN;
        if (!isOwner && !isAdmin) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException(
                    "Only the course instructor can manage lessons");
        }
    }
}
