package com.codemagic.catalog.admin.domain.exceptions;

import com.codemagic.catalog.admin.domain.validation.Error;

import java.util.List;

public class NotificationException extends DomainException {
    public NotificationException(final List<Error> errors) {
        super(errors);
    }
}
