package com.codemagic.catalog.admin.application.category.create;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import com.codemagic.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.Objects;

import static io.vavr.API.Left;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }
    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var note = Notification.create();

        final var category = Category.newCategory(name, description);
        category.validate(note);

        return note.hasErrors() ? Left(note) : crete(category);
    }

    private Either<Notification, CreateCategoryOutput> crete(Category category) {
        return Try.of(() -> this.gateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);

    }
}
