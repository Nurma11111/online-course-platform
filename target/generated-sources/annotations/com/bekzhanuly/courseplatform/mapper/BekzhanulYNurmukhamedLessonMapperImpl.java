package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedLessonResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedLesson;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-20T03:59:07+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class BekzhanulYNurmukhamedLessonMapperImpl implements BekzhanulYNurmukhamedLessonMapper {

    @Override
    public BekzhanulYNurmukhamedLessonResponse toResponse(BekzhanulYNurmukhamedLesson lesson) {
        if ( lesson == null ) {
            return null;
        }

        BekzhanulYNurmukhamedLessonResponse.BekzhanulYNurmukhamedLessonResponseBuilder bekzhanulYNurmukhamedLessonResponse = BekzhanulYNurmukhamedLessonResponse.builder();

        bekzhanulYNurmukhamedLessonResponse.id( lesson.getId() );
        bekzhanulYNurmukhamedLessonResponse.title( lesson.getTitle() );
        bekzhanulYNurmukhamedLessonResponse.content( lesson.getContent() );
        bekzhanulYNurmukhamedLessonResponse.videoUrl( lesson.getVideoUrl() );
        bekzhanulYNurmukhamedLessonResponse.durationMinutes( lesson.getDurationMinutes() );
        bekzhanulYNurmukhamedLessonResponse.orderIndex( lesson.getOrderIndex() );
        bekzhanulYNurmukhamedLessonResponse.lessonType( lesson.getLessonType() );
        bekzhanulYNurmukhamedLessonResponse.isFreePreview( lesson.getIsFreePreview() );
        bekzhanulYNurmukhamedLessonResponse.createdAt( lesson.getCreatedAt() );

        bekzhanulYNurmukhamedLessonResponse.courseId( lesson.getCourse() != null ? lesson.getCourse().getId() : null );
        bekzhanulYNurmukhamedLessonResponse.courseTitle( lesson.getCourse() != null ? lesson.getCourse().getTitle() : null );
        bekzhanulYNurmukhamedLessonResponse.attachmentId( lesson.getAttachment() != null ? lesson.getAttachment().getId() : null );
        bekzhanulYNurmukhamedLessonResponse.attachmentFilename( lesson.getAttachment() != null ? lesson.getAttachment().getOriginalFilename() : null );

        return bekzhanulYNurmukhamedLessonResponse.build();
    }

    @Override
    public List<BekzhanulYNurmukhamedLessonResponse> toResponseList(List<BekzhanulYNurmukhamedLesson> lessons) {
        if ( lessons == null ) {
            return null;
        }

        List<BekzhanulYNurmukhamedLessonResponse> list = new ArrayList<BekzhanulYNurmukhamedLessonResponse>( lessons.size() );
        for ( BekzhanulYNurmukhamedLesson bekzhanulYNurmukhamedLesson : lessons ) {
            list.add( toResponse( bekzhanulYNurmukhamedLesson ) );
        }

        return list;
    }
}
