package com.codemagic.catalog.admin.domain.category;

import com.codemagic.catalog.admin.domain.AggregateRoot;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {
    private final String name;
    private final String description;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description) {
        return new Category(CategoryID.unique(),
                name, description,
                true,
                Instant.now(),
                Instant.now(),
                null);
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.updatedAt = Instant.now();
        this.active = true;
        return this;
    }

    public Category deactivate() {
        if (this.deletedAt == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}