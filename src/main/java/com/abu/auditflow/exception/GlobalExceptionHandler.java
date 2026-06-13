package com.abu.auditflow.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(
                                                Instant.now(),
                                                HttpStatus.NOT_FOUND.value(),
                                                ErrorCode.RESOURCE_NOT_FOUND.name(),
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusiness(
                        BusinessException ex,
                        HttpServletRequest request) {

                return ResponseEntity.badRequest()
                                .body(new ErrorResponse(
                                                Instant.now(),
                                                HttpStatus.BAD_REQUEST.value(),
                                                ErrorCode.BUSINESS_ERROR.name(),
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest()
                                .body(new ErrorResponse(
                                                Instant.now(),
                                                HttpStatus.BAD_REQUEST.value(),
                                                ErrorCode.VALIDATION_ERROR.name(),
                                                message,
                                                request.getRequestURI()));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(
                        BadCredentialsException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                401,
                                "INVALID_CREDENTIALS",
                                "Invalid username or password",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(
                        Exception ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(
                                                Instant.now(),
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                ErrorCode.INTERNAL_ERROR.name(),
                                                "An unexpected error occurred",
                                                request.getRequestURI()));
        }
}