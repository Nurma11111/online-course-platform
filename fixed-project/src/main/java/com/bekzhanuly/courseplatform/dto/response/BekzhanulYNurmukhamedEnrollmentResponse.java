package com.bekzhanuly.courseplatform.dto.response;

import com.bekzhanuly.courseplatform.enums.BekzhanulYNurmukhamedEnrollmentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Enrollment Response DTO
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BekzhanulYNurmukhamedEnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private BekzhanulYNurmukhamedEnrollmentStatus status;
    private Integer progressPercentage;
    private BigDecimal pricePaid;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}
