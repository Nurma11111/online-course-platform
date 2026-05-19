package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedEnrollmentResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedEnrollment;
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
public class BekzhanulYNurmukhamedEnrollmentMapperImpl implements BekzhanulYNurmukhamedEnrollmentMapper {

    @Override
    public BekzhanulYNurmukhamedEnrollmentResponse toResponse(BekzhanulYNurmukhamedEnrollment enrollment) {
        if ( enrollment == null ) {
            return null;
        }

        BekzhanulYNurmukhamedEnrollmentResponse.BekzhanulYNurmukhamedEnrollmentResponseBuilder bekzhanulYNurmukhamedEnrollmentResponse = BekzhanulYNurmukhamedEnrollmentResponse.builder();

        bekzhanulYNurmukhamedEnrollmentResponse.id( enrollment.getId() );
        bekzhanulYNurmukhamedEnrollmentResponse.status( enrollment.getStatus() );
        bekzhanulYNurmukhamedEnrollmentResponse.progressPercentage( enrollment.getProgressPercentage() );
        bekzhanulYNurmukhamedEnrollmentResponse.pricePaid( enrollment.getPricePaid() );
        bekzhanulYNurmukhamedEnrollmentResponse.enrolledAt( enrollment.getEnrolledAt() );
        bekzhanulYNurmukhamedEnrollmentResponse.completedAt( enrollment.getCompletedAt() );

        bekzhanulYNurmukhamedEnrollmentResponse.studentId( enrollment.getStudent() != null ? enrollment.getStudent().getId() : null );
        bekzhanulYNurmukhamedEnrollmentResponse.studentName( enrollment.getStudent() != null ? enrollment.getStudent().getFirstName() + ' ' + enrollment.getStudent().getLastName() : null );
        bekzhanulYNurmukhamedEnrollmentResponse.courseId( enrollment.getCourse() != null ? enrollment.getCourse().getId() : null );
        bekzhanulYNurmukhamedEnrollmentResponse.courseTitle( enrollment.getCourse() != null ? enrollment.getCourse().getTitle() : null );

        return bekzhanulYNurmukhamedEnrollmentResponse.build();
    }

    @Override
    public List<BekzhanulYNurmukhamedEnrollmentResponse> toResponseList(List<BekzhanulYNurmukhamedEnrollment> enrollments) {
        if ( enrollments == null ) {
            return null;
        }

        List<BekzhanulYNurmukhamedEnrollmentResponse> list = new ArrayList<BekzhanulYNurmukhamedEnrollmentResponse>( enrollments.size() );
        for ( BekzhanulYNurmukhamedEnrollment bekzhanulYNurmukhamedEnrollment : enrollments ) {
            list.add( toResponse( bekzhanulYNurmukhamedEnrollment ) );
        }

        return list;
    }
}
