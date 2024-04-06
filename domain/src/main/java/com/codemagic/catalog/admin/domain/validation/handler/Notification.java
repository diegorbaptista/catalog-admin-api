package com.codemagic.catalog.admin.domain.validation.handler;

import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {
    private final List<Error> errors;

    private Notification(List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    public static Notification create(final Throwable throwable) {
        return create(new Error(throwable.getMessage()));
    }

    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(Validation<T> validation) {
        try {
            return validation.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Throwable t) {
            this.append(new Error(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

}


