package com.application.ReviewsApplication.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getMessage() != null &&
                ex.getMessage().contains("Parse attempt failed for value")) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use ISO format (YYYY-MM-DD).");
        }

        return ResponseEntity.badRequest().body("Invalid input parameter: " + ex.getMessage());
    }



    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConversionFailedException(ConversionFailedException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("java.time.LocalDate")) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use ISO format (YYYY-MM-DD).");
        }
        return ResponseEntity.badRequest().body("Invalid input value: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        if (ex.getMessage().contains("Parse attempt failed for value")) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use ISO format (YYYY-MM-DD).");
        }
        return ResponseEntity.badRequest().body("Invalid input parameter: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }
}
