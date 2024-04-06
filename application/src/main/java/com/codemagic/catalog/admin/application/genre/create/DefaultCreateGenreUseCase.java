package com.codemagic.catalog.admin.application.genre.create;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    public DefaultCreateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand command) {
        final var name = command.name();
        final var categories = toCategoriesID(command.categories());

        final var notification = Notification.create();
        notification.append(validateCategories(categories));

        final var newGenre = notification.validate(() -> Genre.newGenre(name));

        if (notification.hasErrors()) {
            throw new NotificationException(notification.getErrors());
        }

        newGenre.addCategories(categories);

        this.genreGateway.create(newGenre);
        return CreateGenreOutput.from(newGenre);
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
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
