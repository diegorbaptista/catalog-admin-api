package com.codemagic.catalog.admin.application.category.retrieve.list;

import com.codemagic.catalog.admin.application.category.retrieve.CategoryOutput;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(CategoryID id, String name, String description, boolean isActive, Instant createdAt,
                                 Instant deletedAt) {
    public static CategoryListOutput from(Category category) {
        return new CategoryListOutput(category.getId(), category.getName(), category.getDescription(),
                category.isActive(), category.getCreatedAt(), category.getDeletedAt());
    }
}
