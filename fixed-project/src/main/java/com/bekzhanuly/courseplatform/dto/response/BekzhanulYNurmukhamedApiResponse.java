package com.bekzhanuly.courseplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Generic API Response Wrapper
 * Author: Bekzhanuly Nurmukhamed
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BekzhanulYNurmukhamedApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object errors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> BekzhanulYNurmukhamedApiResponse<T> success(T data, String message) {
        return BekzhanulYNurmukhamedApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BekzhanulYNurmukhamedApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    public static <T> BekzhanulYNurmukhamedApiResponse<T> error(String message, Object errors) {
        return BekzhanulYNurmukhamedApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .build();
    }

    public static <T> BekzhanulYNurmukhamedApiResponse<T> error(String message) {
        return error(message, null);
    }
}
