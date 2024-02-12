package com.codemagic.catalog.admin.application.category.retrieve.get;

import com.codemagic.catalog.admin.domain.category.Category;

import java.time.Instant;

public record CategoryOutput(String id,
                             String name,
                             String description,
                             boolean isActive,
                             Instant createdAt,
                             Instant updatedAt,
                             Instant deletedAt) {
    public static CategoryOutput from(Category category) {
        return new CategoryOutput(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt());
    }
}
