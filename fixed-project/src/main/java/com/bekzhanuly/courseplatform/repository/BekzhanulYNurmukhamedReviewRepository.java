package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Review Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedReviewRepository extends JpaRepository<BekzhanulYNurmukhamedReview, Long> {

    Optional<BekzhanulYNurmukhamedReview> findByUserIdAndCourseId(Long userId, Long courseId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    Page<BekzhanulYNurmukhamedReview> findByCourseId(Long courseId, Pageable pageable);

    Page<BekzhanulYNurmukhamedReview> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM BekzhanulYNurmukhamedReview r WHERE r.course.id = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(r) FROM BekzhanulYNurmukhamedReview r WHERE r.course.id = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);
}
