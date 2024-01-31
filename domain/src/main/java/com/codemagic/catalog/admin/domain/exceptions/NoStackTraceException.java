package com.codemagic.catalog.admin.domain.exceptions;

public class NoStackTraceException extends RuntimeException {
    public NoStackTraceException(String message) {
        super(message, null, true, false);
    }
}
