package com.codemagic.catalog.admin.application.category.create;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;

public record CreateCategoryOutput(CategoryID id) {
    static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
