package com.codemagic.catalog.admin.domain.exceptions;

import com.codemagic.catalog.admin.domain.validation.Error;

import java.util.List;
import java.util.stream.Collectors;

public class DomainException extends NoStackTraceException {
    protected final List<Error> errors;

    protected DomainException(final List<Error> errors) {
        super(errors.stream()
                .map(Error::message)
                .collect(Collectors.joining(", ")));
        this.errors = errors;
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException(errors);
    }

    public static DomainException with(Error error) {
        return new DomainException(List.of(error));
    }

    public List<Error> getErrors() {
        return this.errors;
    }
}
