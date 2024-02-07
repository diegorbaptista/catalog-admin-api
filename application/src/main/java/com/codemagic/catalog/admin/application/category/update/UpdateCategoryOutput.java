package com.codemagic.catalog.admin.application.category.update;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(CategoryID id) {
    static UpdateCategoryOutput from(Category category) {
        return new UpdateCategoryOutput(category.getId());
    }
}
