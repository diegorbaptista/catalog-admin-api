package com.codemagic.catalog.admin.application.category.update;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(String id) {

    public static UpdateCategoryOutput from(String id) {
        return new UpdateCategoryOutput(id);
    }
    public static UpdateCategoryOutput from(Category category) {
        return new UpdateCategoryOutput(category.getId().getValue());
    }
}
