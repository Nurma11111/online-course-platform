package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedEnrollmentResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Enrollment Mapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Mapper(componentModel = "spring")
public interface BekzhanulYNurmukhamedEnrollmentMapper {

    @Mapping(target = "studentId",   expression = "java(enrollment.getStudent() != null ? enrollment.getStudent().getId() : null)")
    @Mapping(target = "studentName", expression = "java(enrollment.getStudent() != null ? enrollment.getStudent().getFirstName() + ' ' + enrollment.getStudent().getLastName() : null)")
    @Mapping(target = "courseId",    expression = "java(enrollment.getCourse() != null ? enrollment.getCourse().getId() : null)")
    @Mapping(target = "courseTitle", expression = "java(enrollment.getCourse() != null ? enrollment.getCourse().getTitle() : null)")
    BekzhanulYNurmukhamedEnrollmentResponse toResponse(BekzhanulYNurmukhamedEnrollment enrollment);

    List<BekzhanulYNurmukhamedEnrollmentResponse> toResponseList(List<BekzhanulYNurmukhamedEnrollment> enrollments);
}
