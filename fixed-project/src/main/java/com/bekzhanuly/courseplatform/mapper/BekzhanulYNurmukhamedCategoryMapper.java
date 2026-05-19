package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCategoryResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Category Mapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Mapper(componentModel = "spring")
public interface BekzhanulYNurmukhamedCategoryMapper {

    @Mapping(target = "totalCourses", expression = "java(category.getCourses() != null ? category.getCourses().size() : 0)")
    BekzhanulYNurmukhamedCategoryResponse toResponse(BekzhanulYNurmukhamedCategory category);

    List<BekzhanulYNurmukhamedCategoryResponse> toResponseList(List<BekzhanulYNurmukhamedCategory> categories);
}
