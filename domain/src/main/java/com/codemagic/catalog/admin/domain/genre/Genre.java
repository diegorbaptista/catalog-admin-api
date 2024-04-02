package com.codemagic.catalog.admin.domain.genre;

import com.codemagic.catalog.admin.domain.AggregateRoot;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.util.InstantUtil;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Genre(final GenreID id, final String name, final boolean isActive, final List<CategoryID> categories,
                   final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        super(id);
        this.name = name;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        validate();
    }

    private void validate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException(notification.getErrors());
        }
    }

    public static Genre newGenre(final String name) {
        final var id = GenreID.unique();
        final var now = InstantUtil.now();
        return new Genre(id, name, true, new ArrayList<>(), now, now, null);
    }

    public Genre update(final String name, final boolean active, final List<CategoryID> categories) {
        this.name = name;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        if (active) {
            activate();
        } else {
            deactivate();
        }
        validate();
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public void activate() {
        if (!active) {
            active = true;
            updatedAt = InstantUtil.now();
            deletedAt = null;
        }
    }

    public void deactivate() {
        if (active) {
            active = false;
            updatedAt = InstantUtil.now();
            deletedAt = updatedAt;
        }
    }

    public Genre with(final GenreID id, final String name, final boolean isActive, final List<CategoryID> categories,
                 final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        return new Genre(id, name, isActive, categories, createdAt, updatedAt, deletedAt);
    }

    public Genre with(final Genre genre) {
        return new Genre(
                genre.getId(),
                genre.getName(),
                genre.isActive(),
                new ArrayList<>(getCategories()),
                genre.getCreatedAt(),
                genre.getUpdatedAt(),
                genre.getDeletedAt());
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
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

    public Genre addCategory(CategoryID categoryID) {
        if (categoryID != null) {
            this.categories.add(categoryID);
            this.updatedAt = InstantUtil.now();
        }
        return this;
    }

    public Genre removeCategory(CategoryID categoryID) {
        if (categoryID != null) {
            this.categories.remove(categoryID);
            this.updatedAt = InstantUtil.now();
        }
        return this;

    }
}
