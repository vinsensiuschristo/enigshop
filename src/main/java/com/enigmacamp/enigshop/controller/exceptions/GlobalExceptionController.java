package com.enigmacamp.enigshop.controller.exceptions;

import com.enigmacamp.enigshop.exceptions.DuplicateResourceException;
import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.dto.response.CommonResponse;
import com.enigmacamp.enigshop.models.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<String>> handleResourceNotFound(ResourceNotFoundException e) {
        CommonResponse<String> response = new CommonResponse<>(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<CommonResponse<String>> handleRequestValidation(RequestValidationException e) {
        CommonResponse<String> response = new CommonResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Parameter '%s' memiliki nilai yang tidak valid: '%s'. Harap masukkan nilai yang valid.",
                ex.getName(), ex.getValue());

        return new ResponseEntity<>(new CommonResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, null,null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CommonResponse<String>> handleDuplicateResource(DuplicateResourceException e) {
        CommonResponse<String> response = new CommonResponse<>(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
