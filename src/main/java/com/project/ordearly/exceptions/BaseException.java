package com.project.ordearly.exceptions;

public abstract class BaseException extends RuntimeException {
    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String format, Object... args) {
        super(String.format(format, args));
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}