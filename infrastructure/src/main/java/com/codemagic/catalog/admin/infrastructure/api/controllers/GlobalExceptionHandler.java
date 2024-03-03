package com.codemagic.catalog.admin.infrastructure.api.controllers;

import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ApiError(String message, List<Error> errors) {
        static ApiError with(DomainException exception) {
            return new ApiError(exception.getMessage(), exception.getErrors());
        }
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity().body(ApiError.with(exception));
    }

}
