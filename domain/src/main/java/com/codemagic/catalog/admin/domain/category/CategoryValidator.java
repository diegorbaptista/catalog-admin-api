package com.codemagic.catalog.admin.domain.category;

import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;
    protected CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }
    @Override
    public void validate() {
        if (this.category.getName() == null) {
            this.handler().append(new Error("'name' should not be null"));
        }
        if (this.category.getName().isBlank()) {
            this.handler().append(new Error("'name' should not be empty"));
        }
        if ((this.category.getName().trim().length() < 3) || (this.category.getName().trim().length() > 255)) {
            this.handler().append(new Error("'name' length must be between 3 and 255 characters"));
        }
    }
}
