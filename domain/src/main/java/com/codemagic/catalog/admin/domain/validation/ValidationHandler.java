package com.codemagic.catalog.admin.domain.validation;

import java.util.List;

public interface ValidationHandler {
        ValidationHandler append(Error error);
        ValidationHandler append(ValidationHandler handler);
        <T> T validate(Validation<T> validation);
        List<Error> getErrors();
        default boolean hasErrors() {
            return getErrors() != null && !getErrors().isEmpty();
        }
        default Error first() {
            if ((getErrors() != null) && (!getErrors().isEmpty())) {
                return getErrors().get(0);
            }
            return null;
        }
        interface Validation<T> {
            T validate();
        }
}
