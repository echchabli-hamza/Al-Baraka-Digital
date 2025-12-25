package com.AlBaraka.demo.exeption;

import com.AlBaraka.demo.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {


        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI(),
                "Email is already in use",
                "EMAIL_ALREADY_EXISTS"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                ex.getMessage(),
                "INVALID_REQUEST"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(),
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOperationNotFound(
            OperationNotFoundException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                422,
                request.getRequestURI(),
                ex.getMessage(),
                "ENTITY_NOT_FOUND"
        );

        return ResponseEntity.status(422).body(response);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleClientNotFound(
            OperationNotFoundException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                422,
                request.getRequestURI(),
                ex.getMessage(),
                "ENTITY_NOT_FOUND"
        );

        return ResponseEntity.status(422).body(response);
    }



}