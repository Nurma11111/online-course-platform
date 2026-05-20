package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedCourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Course Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedCourseRepository extends JpaRepository<BekzhanulYNurmukhamedCourse, Long> {

    /**
     * Advanced search with pagination, sorting, filtering
     */
    @Query("SELECT c FROM BekzhanulYNurmukhamedCourse c WHERE " +
           "(:search IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:categoryId IS NULL OR c.category.id = :categoryId) " +
           "AND (:instructorId IS NULL OR c.instructor.id = :instructorId) " +
           "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
           "AND (:language IS NULL OR LOWER(c.language) = LOWER(:language)) " +
           "AND (:difficulty IS NULL OR LOWER(c.difficultyLevel) = LOWER(:difficulty))")
    Page<BekzhanulYNurmukhamedCourse> findAllWithFilters(
            @Param("search") String search,
            @Param("status") BekzhanulYNurmukhamedCourseStatus status,
            @Param("categoryId") Long categoryId,
            @Param("instructorId") Long instructorId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("language") String language,
            @Param("difficulty") String difficulty,
            Pageable pageable);

    List<BekzhanulYNurmukhamedCourse> findByInstructorId(Long instructorId);

    @Query("SELECT c FROM BekzhanulYNurmukhamedCourse c WHERE c.instructor.id = :instructorId")
    Page<BekzhanulYNurmukhamedCourse> findByInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);

    boolean existsByTitleAndInstructorId(String title, Long instructorId);

    @Query("SELECT COUNT(c) FROM BekzhanulYNurmukhamedCourse c WHERE c.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM BekzhanulYNurmukhamedCourse c ORDER BY c.totalStudents DESC")
    List<BekzhanulYNurmukhamedCourse> findTopCoursesByEnrollment(Pageable pageable);
}
