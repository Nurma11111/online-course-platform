package com.bekzhanuly.courseplatform.repository;

import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedFileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * FileAttachment Repository
 * Author: Bekzhanuly Nurmukhamed
 */
@Repository
public interface BekzhanulYNurmukhamedFileAttachmentRepository extends JpaRepository<BekzhanulYNurmukhamedFileAttachment, Long> {
    Optional<BekzhanulYNurmukhamedFileAttachment> findByStoredFilename(String storedFilename);
    Optional<BekzhanulYNurmukhamedFileAttachment> findByLessonId(Long lessonId);
}
