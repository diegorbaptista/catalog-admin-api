package com.codemagic.catalog.admin.domain.validation;

public abstract class Validator {
    private final ValidationHandler handler;
    protected Validator(ValidationHandler handler) {
        this.handler = handler;
    }

    protected ValidationHandler handler() {
        return this.handler;
    }

    public abstract void validate();
}
