package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.domain.validation.ValidationHandler;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    protected AggregateRoot(final ID id) {
        super(id);
    }
}
