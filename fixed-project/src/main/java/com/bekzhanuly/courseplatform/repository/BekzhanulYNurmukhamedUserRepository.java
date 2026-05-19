package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedUserRepository extends JpaRepository<BekzhanulYNurmukhamedUser, Long> {

    Optional<BekzhanulYNurmukhamedUser> findByUsername(String username);

    Optional<BekzhanulYNurmukhamedUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM BekzhanulYNurmukhamedUser u WHERE " +
           "u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<BekzhanulYNurmukhamedUser> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    @Query("SELECT u FROM BekzhanulYNurmukhamedUser u WHERE " +
           "(:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')))" +
           "AND (:role IS NULL OR u.role = :role)")
    Page<BekzhanulYNurmukhamedUser> findAllWithFilters(
            @Param("search") String search,
            @Param("role") BekzhanulYNurmukhamedRole role,
            Pageable pageable);
}
