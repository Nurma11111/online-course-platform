package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedReviewResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedReview;
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
public class BekzhanulYNurmukhamedReviewMapperImpl implements BekzhanulYNurmukhamedReviewMapper {

    @Override
    public BekzhanulYNurmukhamedReviewResponse toResponse(BekzhanulYNurmukhamedReview review) {
        if ( review == null ) {
            return null;
        }

        BekzhanulYNurmukhamedReviewResponse.BekzhanulYNurmukhamedReviewResponseBuilder bekzhanulYNurmukhamedReviewResponse = BekzhanulYNurmukhamedReviewResponse.builder();

        bekzhanulYNurmukhamedReviewResponse.id( review.getId() );
        bekzhanulYNurmukhamedReviewResponse.rating( review.getRating() );
        bekzhanulYNurmukhamedReviewResponse.comment( review.getComment() );
        bekzhanulYNurmukhamedReviewResponse.createdAt( review.getCreatedAt() );

        bekzhanulYNurmukhamedReviewResponse.userId( review.getUser() != null ? review.getUser().getId() : null );
        bekzhanulYNurmukhamedReviewResponse.username( review.getUser() != null ? review.getUser().getUsername() : null );
        bekzhanulYNurmukhamedReviewResponse.courseId( review.getCourse() != null ? review.getCourse().getId() : null );
        bekzhanulYNurmukhamedReviewResponse.courseTitle( review.getCourse() != null ? review.getCourse().getTitle() : null );

        return bekzhanulYNurmukhamedReviewResponse.build();
    }

    @Override
    public List<BekzhanulYNurmukhamedReviewResponse> toResponseList(List<BekzhanulYNurmukhamedReview> reviews) {
        if ( reviews == null ) {
            return null;
        }

        List<BekzhanulYNurmukhamedReviewResponse> list = new ArrayList<BekzhanulYNurmukhamedReviewResponse>( reviews.size() );
        for ( BekzhanulYNurmukhamedReview bekzhanulYNurmukhamedReview : reviews ) {
            list.add( toResponse( bekzhanulYNurmukhamedReview ) );
        }

        return list;
    }
}
