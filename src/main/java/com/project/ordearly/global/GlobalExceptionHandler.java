package com.project.ordearly.global;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import com.project.ordearly.exceptions.ErrorResponse;
import com.project.ordearly.exceptions.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private String sanitizeMessage(String message) {
        if (message == null)
            return "An error occurred";
        return HtmlUtils.htmlEscape(message);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, HttpServletRequest request) {
        var status = HttpStatus.UNAUTHORIZED;

        var error = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(sanitizeMessage(ex.getMessage()))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}