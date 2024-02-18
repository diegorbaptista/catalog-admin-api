package com.codemagic.catalog.admin.domain.category;

import com.codemagic.catalog.admin.domain.AggregateRoot;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
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

    public static Category with(
            final CategoryID id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Category(
                id,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
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

    public Category update(final String name,
                           final String description,
                           final boolean isActive) {
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        if (isActive) {
            this.activate();
        } else {
            this.deactivate();
        }
        return this;
    }
    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}