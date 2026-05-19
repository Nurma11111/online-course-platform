package com.bekzhanuly.courseplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception Classes
 * Author: Bekzhanuly Nurmukhamed
 */
public class BekzhanulYNurmukhamedExceptions {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class BekzhanulYNurmukhamedResourceNotFoundException extends RuntimeException {
        public BekzhanulYNurmukhamedResourceNotFoundException(String message) { super(message); }
        public BekzhanulYNurmukhamedResourceNotFoundException(String resource, Long id) {
            super(resource + " not found with id: " + id);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class BekzhanulYNurmukhamedAlreadyExistsException extends RuntimeException {
        public BekzhanulYNurmukhamedAlreadyExistsException(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class BekzhanulYNurmukhamedAccessDeniedException extends RuntimeException {
        public BekzhanulYNurmukhamedAccessDeniedException(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BekzhanulYNurmukhamedBadRequestException extends RuntimeException {
        public BekzhanulYNurmukhamedBadRequestException(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BekzhanulYNurmukhamedFileStorageException extends RuntimeException {
        public BekzhanulYNurmukhamedFileStorageException(String message) { super(message); }
        public BekzhanulYNurmukhamedFileStorageException(String message, Throwable cause) { super(message, cause); }
    }
}
