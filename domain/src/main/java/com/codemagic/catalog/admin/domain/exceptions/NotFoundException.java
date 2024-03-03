package com.codemagic.catalog.admin.domain.exceptions;

import com.codemagic.catalog.admin.domain.AggregateRoot;
import com.codemagic.catalog.admin.domain.Identifier;
import com.codemagic.catalog.admin.domain.validation.Error;

import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(List<Error> errors) {
        super(errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregateRoot,
            final Identifier id) {
        return new NotFoundException(List.of(new Error(
                "%s with ID %s was not found".formatted(aggregateRoot.getSimpleName(), id.getValue())
        )));
    }

}
