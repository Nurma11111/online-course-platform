package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedEnrollment;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedEnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Enrollment Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedEnrollmentRepository extends JpaRepository<BekzhanulYNurmukhamedEnrollment, Long> {

    Optional<BekzhanulYNurmukhamedEnrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Page<BekzhanulYNurmukhamedEnrollment> findByStudentId(Long studentId, Pageable pageable);

    Page<BekzhanulYNurmukhamedEnrollment> findByCourseId(Long courseId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM BekzhanulYNurmukhamedEnrollment e WHERE e.course.id = :courseId AND e.status = :status")
    long countByCourseIdAndStatus(@Param("courseId") Long courseId, @Param("status") BekzhanulYNurmukhamedEnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM BekzhanulYNurmukhamedEnrollment e WHERE e.student.id = :studentId AND e.status = 'COMPLETED'")
    long countCompletedByStudentId(@Param("studentId") Long studentId);
}
