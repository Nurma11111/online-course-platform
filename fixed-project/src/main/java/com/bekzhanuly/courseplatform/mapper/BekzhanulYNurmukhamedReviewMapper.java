package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedReviewResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Review Mapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Mapper(componentModel = "spring")
public interface BekzhanulYNurmukhamedReviewMapper {

    @Mapping(target = "userId",     expression = "java(review.getUser() != null ? review.getUser().getId() : null)")
    @Mapping(target = "username",   expression = "java(review.getUser() != null ? review.getUser().getUsername() : null)")
    @Mapping(target = "courseId",   expression = "java(review.getCourse() != null ? review.getCourse().getId() : null)")
    @Mapping(target = "courseTitle",expression = "java(review.getCourse() != null ? review.getCourse().getTitle() : null)")
    BekzhanulYNurmukhamedReviewResponse toResponse(BekzhanulYNurmukhamedReview review);

    List<BekzhanulYNurmukhamedReviewResponse> toResponseList(List<BekzhanulYNurmukhamedReview> reviews);
}
