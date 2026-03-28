package com.project.login_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate resource")
public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}