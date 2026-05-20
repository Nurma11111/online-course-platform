package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Category Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedCategoryRepository extends JpaRepository<BekzhanulYNurmukhamedCategory, Long> {
    Optional<BekzhanulYNurmukhamedCategory> findByName(String name);
    boolean existsByName(String name);
    List<BekzhanulYNurmukhamedCategory> findByIsActiveTrue();
}
