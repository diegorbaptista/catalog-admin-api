package com.codemagic.catalog.admin.domain.exceptions;

public class InternalErrorException extends NoStackTraceException {

    public InternalErrorException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public static InternalErrorException with(final String message, final Throwable throwable) {
        return new InternalErrorException(message, throwable);
    }
}
