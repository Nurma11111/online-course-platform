package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedLessonResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedLesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Lesson Mapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Mapper(componentModel = "spring")
public interface BekzhanulYNurmukhamedLessonMapper {

    @Mapping(target = "courseId",           expression = "java(lesson.getCourse() != null ? lesson.getCourse().getId() : null)")
    @Mapping(target = "courseTitle",        expression = "java(lesson.getCourse() != null ? lesson.getCourse().getTitle() : null)")
    @Mapping(target = "attachmentId",       expression = "java(lesson.getAttachment() != null ? lesson.getAttachment().getId() : null)")
    @Mapping(target = "attachmentFilename", expression = "java(lesson.getAttachment() != null ? lesson.getAttachment().getOriginalFilename() : null)")
    BekzhanulYNurmukhamedLessonResponse toResponse(BekzhanulYNurmukhamedLesson lesson);

    List<BekzhanulYNurmukhamedLessonResponse> toResponseList(List<BekzhanulYNurmukhamedLesson> lessons);
}
