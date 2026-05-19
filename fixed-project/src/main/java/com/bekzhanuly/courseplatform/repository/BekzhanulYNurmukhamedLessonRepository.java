package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Lesson Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedLessonRepository extends JpaRepository<BekzhanulYNurmukhamedLesson, Long> {

    List<BekzhanulYNurmukhamedLesson> findByCourseIdOrderByOrderIndex(Long courseId);

    @Query("SELECT COUNT(l) FROM BekzhanulYNurmukhamedLesson l WHERE l.course.id = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COALESCE(MAX(l.orderIndex), 0) FROM BekzhanulYNurmukhamedLesson l WHERE l.course.id = :courseId")
    Integer findMaxOrderIndexByCourseId(@Param("courseId") Long courseId);
}
