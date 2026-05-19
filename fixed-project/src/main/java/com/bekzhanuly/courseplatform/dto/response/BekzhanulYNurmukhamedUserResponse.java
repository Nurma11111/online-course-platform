package com.bekzhanuly.courseplatform.dto.response;

import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedRole;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedUserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String bio;
    private BekzhanulYNurmukhamedRole role;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private LocalDateTime createdAt;
}
