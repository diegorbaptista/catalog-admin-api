package com.codemagic.catalog.admin.domain.category;

import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 255;
    private final Category category;
    protected CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }
    @Override
    public void validate() {
        final var name = this.category.getName();
        if (name == null) {
            this.handler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.handler().append(new Error("'name' should not be empty"));
            return;
        }
        if ((name.trim().length() < MIN_NAME_LENGTH) || (name.trim().length() > MAX_NAME_LENGTH)) {
            this.handler().append(new Error("'name' length must be between 3 and 255 characters"));
        }
    }
}
