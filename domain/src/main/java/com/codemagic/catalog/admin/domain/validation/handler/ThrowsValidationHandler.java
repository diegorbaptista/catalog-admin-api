package com.codemagic.catalog.admin.domain.validation.handler;

import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(List.of(error));
    }
    @Override
    public ValidationHandler append(ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }
    @Override
    public <T> T validate(Validation<T> validation) {
        try {
            validation.validate();
        } catch (Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }
}
