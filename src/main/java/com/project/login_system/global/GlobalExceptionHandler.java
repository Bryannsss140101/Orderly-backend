package com.project.login_system.global;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import com.project.login_system.exceptions.DuplicateResourceException;
import com.project.login_system.exceptions.EmailSendException;
import com.project.login_system.exceptions.ErrorResponse;
import com.project.login_system.exceptions.ResourceNotFoundException;
import com.project.login_system.exceptions.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private String sanitizeMessage(String message) {
        if (message == null)
            return "An error occurred";
        return HtmlUtils.htmlEscape(message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Resource not found",
                sanitizeMessage(ex.getMessage()),
                sanitizeMessage(request.getRequestURI()));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        var status = HttpStatus.CONFLICT;
        var error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Duplicate resource",
                sanitizeMessage(ex.getMessage()),
                sanitizeMessage(request.getRequestURI()));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendException(
            EmailSendException ex, HttpServletRequest request) {
        var status = HttpStatus.SERVICE_UNAVAILABLE;
        var error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Service Unavailable",
                sanitizeMessage(ex.getMessage()),
                sanitizeMessage(request.getRequestURI()));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, HttpServletRequest request) {
        var status = HttpStatus.UNAUTHORIZED;
        var error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Unauthorized",
                sanitizeMessage(ex.getMessage()),
                sanitizeMessage(request.getRequestURI()));

        return ResponseEntity.status(status).body(error);
    }
}