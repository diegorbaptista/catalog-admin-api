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
    }
}
