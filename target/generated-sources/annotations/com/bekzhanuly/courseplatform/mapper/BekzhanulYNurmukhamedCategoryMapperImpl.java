package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCategoryResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCategory;
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
public class BekzhanulYNurmukhamedCategoryMapperImpl implements BekzhanulYNurmukhamedCategoryMapper {

    @Override
    public BekzhanulYNurmukhamedCategoryResponse toResponse(BekzhanulYNurmukhamedCategory category) {
        if ( category == null ) {
            return null;
        }

        BekzhanulYNurmukhamedCategoryResponse.BekzhanulYNurmukhamedCategoryResponseBuilder bekzhanulYNurmukhamedCategoryResponse = BekzhanulYNurmukhamedCategoryResponse.builder();

        bekzhanulYNurmukhamedCategoryResponse.id( category.getId() );
        bekzhanulYNurmukhamedCategoryResponse.name( category.getName() );
        bekzhanulYNurmukhamedCategoryResponse.description( category.getDescription() );
        bekzhanulYNurmukhamedCategoryResponse.iconUrl( category.getIconUrl() );
        bekzhanulYNurmukhamedCategoryResponse.isActive( category.getIsActive() );
        bekzhanulYNurmukhamedCategoryResponse.createdAt( category.getCreatedAt() );

        bekzhanulYNurmukhamedCategoryResponse.totalCourses( category.getCourses() != null ? category.getCourses().size() : 0 );

        return bekzhanulYNurmukhamedCategoryResponse.build();
    }

    @Override
    public List<BekzhanulYNurmukhamedCategoryResponse> toResponseList(List<BekzhanulYNurmukhamedCategory> categories) {
        if ( categories == null ) {
            return null;
        }

        List<BekzhanulYNurmukhamedCategoryResponse> list = new ArrayList<BekzhanulYNurmukhamedCategoryResponse>( categories.size() );
        for ( BekzhanulYNurmukhamedCategory bekzhanulYNurmukhamedCategory : categories ) {
            list.add( toResponse( bekzhanulYNurmukhamedCategory ) );
        }

        return list;
    }
}
