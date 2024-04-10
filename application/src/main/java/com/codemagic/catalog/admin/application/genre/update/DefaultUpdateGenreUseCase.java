package com.codemagic.catalog.admin.application.genre.update;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultUpdateGenreUseCase(final GenreGateway genreGateway, final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand command) {
        final var id = GenreID.from(command.id());
        final var name = command.name();
        final var active = command.active();
        final var categories = toCategoriesID(command.categories());

        final var notification = Notification.create();
        final var genre = this.genreGateway.findById(id)
                .orElseThrow(() -> DomainException.with(new Error("Genre does not exists")));

        notification.append(validateCategories(categories));

        notification.validate(() -> genre.update(name, active, categories));
        if (notification.hasErrors()) {
            throw new NotificationException(notification.getErrors());
        }

        return UpdateGenreOutput.from(this.genreGateway.update(genre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> categories) {
        final var notification = Notification.create();
        if (categories == null || categories.isEmpty()) {
            return notification;
        }

        final var categoriesNotFound = new ArrayList<>(categories);
        final var foundCategories = this.categoryGateway.existsByIds(categories);

        categoriesNotFound.removeAll(foundCategories);
        if (categoriesNotFound.isEmpty()) {
            return notification;
        }

        final var missingIds = categoriesNotFound
                .stream()
                .map(CategoryID::getValue)
                .collect(Collectors.joining(", "));

        notification.append(new Error("Some categories could not be found: %s".formatted(missingIds)));
        return notification;
    }

    private List<CategoryID> toCategoriesID(final List<String> categories) {
        return categories
                .stream()
                .map(CategoryID::from)
                .toList();
    }
}
