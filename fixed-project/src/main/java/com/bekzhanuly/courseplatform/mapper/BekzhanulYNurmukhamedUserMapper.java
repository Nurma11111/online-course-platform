package com.bekzhanuly.courseplatform.mapper;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedUserResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * User Mapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Mapper(componentModel = "spring")
public interface BekzhanulYNurmukhamedUserMapper {

    BekzhanulYNurmukhamedUserResponse toResponse(BekzhanulYNurmukhamedUser user);

    List<BekzhanulYNurmukhamedUserResponse> toResponseList(List<BekzhanulYNurmukhamedUser> users);
}
