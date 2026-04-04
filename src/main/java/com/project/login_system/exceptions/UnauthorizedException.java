package com.project.login_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String format, Object... args) {
        super(String.format(format, args));
    }
}