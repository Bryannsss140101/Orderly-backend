package com.project.login_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Error sending email")
public class EmailSendException extends BaseException {
    public EmailSendException(String message) {
        super(message);
    }

    public EmailSendException(String format, Object... args) {
        super(String.format(format, args));
    }
}