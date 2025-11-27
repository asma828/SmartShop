package com.example.SmartShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request){
    ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);



    }
}