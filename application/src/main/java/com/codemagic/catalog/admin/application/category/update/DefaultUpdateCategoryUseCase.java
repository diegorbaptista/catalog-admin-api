package com.codemagic.catalog.admin.application.category.update;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.Objects;

import static io.vavr.API.Left;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
    private final CategoryGateway gateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway gateway) {
        Objects.requireNonNull(gateway);
        this.gateway = gateway;
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand command) {
       final var id = CategoryID.from(command.id());
       final var name = command.name();
       final var description = command.description();
       final var isActive = command.isActive();

       final var note = Notification.create();
       final var category = this.gateway.findById(id).orElseThrow(() -> categoryNotFound(id));
       category.update(name, description, isActive)
               .validate(note);

        return note.hasErrors() ? Left(note) : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category category) {
        return Try.of(() -> this.gateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static DomainException categoryNotFound(CategoryID id) {
        return DomainException.with(new Error("Category with ID %s was not found".formatted(id.getValue())));
    }
}
