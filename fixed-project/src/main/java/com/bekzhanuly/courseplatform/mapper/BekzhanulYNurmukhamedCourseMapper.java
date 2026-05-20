package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCourseResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Course Mapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Mapper(componentModel = "spring")
public interface BekzhanulYNurmukhamedCourseMapper {

    @Mapping(target = "instructorId",   expression = "java(course.getInstructor() != null ? course.getInstructor().getId() : null)")
    @Mapping(target = "instructorName", expression = "java(course.getInstructor() != null ? course.getInstructor().getFirstName() : null)")
    @Mapping(target = "categoryId",     expression = "java(course.getCategory() != null ? course.getCategory().getId() : null)")
    @Mapping(target = "categoryName",   expression = "java(course.getCategory() != null ? course.getCategory().getName() : null)")
    @Mapping(target = "totalLessons",   expression = "java(course.getLessons() != null ? course.getLessons().size() : 0)")
    BekzhanulYNurmukhamedCourseResponse toResponse(BekzhanulYNurmukhamedCourse course);

    List<BekzhanulYNurmukhamedCourseResponse> toResponseList(List<BekzhanulYNurmukhamedCourse> courses);
}
