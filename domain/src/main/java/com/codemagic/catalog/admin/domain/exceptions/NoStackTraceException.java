package com.codemagic.catalog.admin.domain.exceptions;

public class NoStackTraceException extends RuntimeException {
    public NoStackTraceException(String message) {
        super(message, null, true, false);
    }

    public NoStackTraceException(final String message, Throwable throwable) {
        super(message, throwable, true, false);
    }
}
