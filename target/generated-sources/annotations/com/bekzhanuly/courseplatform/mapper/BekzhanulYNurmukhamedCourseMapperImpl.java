package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCourseResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCourse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-20T03:14:52+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class BekzhanulYNurmukhamedCourseMapperImpl implements BekzhanulYNurmukhamedCourseMapper {

    @Override
    public BekzhanulYNurmukhamedCourseResponse toResponse(BekzhanulYNurmukhamedCourse course) {
        if ( course == null ) {
            return null;
        }

        BekzhanulYNurmukhamedCourseResponse.BekzhanulYNurmukhamedCourseResponseBuilder bekzhanulYNurmukhamedCourseResponse = BekzhanulYNurmukhamedCourseResponse.builder();

        bekzhanulYNurmukhamedCourseResponse.id( course.getId() );
        bekzhanulYNurmukhamedCourseResponse.title( course.getTitle() );
        bekzhanulYNurmukhamedCourseResponse.description( course.getDescription() );
        bekzhanulYNurmukhamedCourseResponse.shortDescription( course.getShortDescription() );
        bekzhanulYNurmukhamedCourseResponse.thumbnailUrl( course.getThumbnailUrl() );
        bekzhanulYNurmukhamedCourseResponse.price( course.getPrice() );
        bekzhanulYNurmukhamedCourseResponse.status( course.getStatus() );
        bekzhanulYNurmukhamedCourseResponse.language( course.getLanguage() );
        bekzhanulYNurmukhamedCourseResponse.difficultyLevel( course.getDifficultyLevel() );
        bekzhanulYNurmukhamedCourseResponse.durationHours( course.getDurationHours() );
        bekzhanulYNurmukhamedCourseResponse.rating( course.getRating() );
        bekzhanulYNurmukhamedCourseResponse.totalReviews( course.getTotalReviews() );
        bekzhanulYNurmukhamedCourseResponse.totalStudents( course.getTotalStudents() );
        bekzhanulYNurmukhamedCourseResponse.createdAt( course.getCreatedAt() );
        bekzhanulYNurmukhamedCourseResponse.updatedAt( course.getUpdatedAt() );

        bekzhanulYNurmukhamedCourseResponse.instructorId( course.getInstructor() != null ? course.getInstructor().getId() : null );
        bekzhanulYNurmukhamedCourseResponse.instructorName( course.getInstructor() != null ? course.getInstructor().getFirstName() : null );
        bekzhanulYNurmukhamedCourseResponse.categoryId( course.getCategory() != null ? course.getCategory().getId() : null );
        bekzhanulYNurmukhamedCourseResponse.categoryName( course.getCategory() != null ? course.getCategory().getName() : null );
        bekzhanulYNurmukhamedCourseResponse.totalLessons( course.getLessons() != null ? course.getLessons().size() : 0 );

        return bekzhanulYNurmukhamedCourseResponse.build();
    }

    @Override
    public List<BekzhanulYNurmukhamedCourseResponse> toResponseList(List<BekzhanulYNurmukhamedCourse> courses) {
        if ( courses == null ) {
            return null;
        }

        List<BekzhanulYNurmukhamedCourseResponse> list = new ArrayList<BekzhanulYNurmukhamedCourseResponse>( courses.size() );
        for ( BekzhanulYNurmukhamedCourse bekzhanulYNurmukhamedCourse : courses ) {
            list.add( toResponse( bekzhanulYNurmukhamedCourse ) );
        }

        return list;
    }
}
