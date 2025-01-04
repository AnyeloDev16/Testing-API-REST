package com.skydev.prueba_testing_API_REST.presentation.advice;

import com.skydev.prueba_testing_API_REST.service.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFound(ResourceNotFoundException rnfe) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(rnfe.getMessage());

    }

}
