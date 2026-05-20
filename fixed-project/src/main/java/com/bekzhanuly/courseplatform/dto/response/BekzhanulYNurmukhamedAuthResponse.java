package com.bekzhanuly.courseplatform.dto.response;

import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedRole;
import lombok.*;

/**
 * Auth Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private Long userId;
    private String username;
    private String email;
    private BekzhanulYNurmukhamedRole role;
}
