package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedUserResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
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
public class BekzhanulYNurmukhamedUserMapperImpl implements BekzhanulYNurmukhamedUserMapper {

    @Override
    public BekzhanulYNurmukhamedUserResponse toResponse(BekzhanulYNurmukhamedUser user) {
        if ( user == null ) {
            return null;
        }

        BekzhanulYNurmukhamedUserResponse.BekzhanulYNurmukhamedUserResponseBuilder bekzhanulYNurmukhamedUserResponse = BekzhanulYNurmukhamedUserResponse.builder();

        bekzhanulYNurmukhamedUserResponse.id( user.getId() );
        bekzhanulYNurmukhamedUserResponse.username( user.getUsername() );
        bekzhanulYNurmukhamedUserResponse.email( user.getEmail() );
        bekzhanulYNurmukhamedUserResponse.firstName( user.getFirstName() );
        bekzhanulYNurmukhamedUserResponse.lastName( user.getLastName() );
        bekzhanulYNurmukhamedUserResponse.profilePicture( user.getProfilePicture() );
        bekzhanulYNurmukhamedUserResponse.bio( user.getBio() );
        bekzhanulYNurmukhamedUserResponse.role( user.getRole() );
        bekzhanulYNurmukhamedUserResponse.isActive( user.getIsActive() );
        bekzhanulYNurmukhamedUserResponse.isEmailVerified( user.getIsEmailVerified() );
        bekzhanulYNurmukhamedUserResponse.createdAt( user.getCreatedAt() );

        return bekzhanulYNurmukhamedUserResponse.build();
    }

    @Override
    public List<BekzhanulYNurmukhamedUserResponse> toResponseList(List<BekzhanulYNurmukhamedUser> users) {
        if ( users == null ) {
            return null;
        }

        List<BekzhanulYNurmukhamedUserResponse> list = new ArrayList<BekzhanulYNurmukhamedUserResponse>( users.size() );
        for ( BekzhanulYNurmukhamedUser bekzhanulYNurmukhamedUser : users ) {
            list.add( toResponse( bekzhanulYNurmukhamedUser ) );
        }

        return list;
    }
}
