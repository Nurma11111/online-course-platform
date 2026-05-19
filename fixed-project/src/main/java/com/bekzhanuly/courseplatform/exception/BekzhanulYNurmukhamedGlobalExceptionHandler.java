package com.bekzhanuly.courseplatform.exception;

import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * Author: Bekzhanuly Nurmukhamed
 */
@RestControllerAdvice
@Slf4j
public class BekzhanulYNurmukhamedGlobalExceptionHandler {

    @ExceptionHandler(BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedResourceNotFoundException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleNotFound(
            BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BekzhanulYNurmukhamedApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleConflict(
            BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BekzhanulYNurmukhamedApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleForbidden(
            BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BekzhanulYNurmukhamedApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedBadRequestException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleBadRequest(
            BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedBadRequestException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BekzhanulYNurmukhamedApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedFileStorageException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleFileStorage(
            BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedFileStorageException ex) {
        log.error("File storage error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BekzhanulYNurmukhamedApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        log.warn("Validation failed: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BekzhanulYNurmukhamedApiResponse.error("Validation failed", errors));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex) {
        log.warn("Bad credentials attempt");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BekzhanulYNurmukhamedApiResponse.error("Invalid username or password"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleSpringAccessDenied(
            AccessDeniedException ex) {
        log.warn("Spring Security access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BekzhanulYNurmukhamedApiResponse.error("Access denied: insufficient permissions"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleMaxUploadSize(
            MaxUploadSizeExceededException ex) {
        log.warn("File too large");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(BekzhanulYNurmukhamedApiResponse.error("File exceeds 50MB limit"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BekzhanulYNurmukhamedApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BekzhanulYNurmukhamedApiResponse.error("An unexpected error occurred. Please try again."));
    }
}
