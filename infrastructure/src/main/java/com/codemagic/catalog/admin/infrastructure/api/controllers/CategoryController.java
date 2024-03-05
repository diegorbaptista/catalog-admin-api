package com.codemagic.catalog.admin.infrastructure.api.controllers;

import com.codemagic.catalog.admin.application.category.create.CreateCategoryCommand;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryOutput;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryCommand;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryOutput;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import com.codemagic.catalog.admin.infrastructure.api.CategoryAPI;
import com.codemagic.catalog.admin.infrastructure.category.models.CreateCategoryApiInput;
import com.codemagic.catalog.admin.infrastructure.category.models.UpdateCategoryApiInput;
import com.codemagic.catalog.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    private final UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
    }
    @Override
    public ResponseEntity<?> create(CreateCategoryApiInput input) {
        final var command = CreateCategoryCommand.with(
                input.name(),
                input.description()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity
                        .created(URI.create("/categories/".concat(output.id())))
                        .body(output);

        return this.createCategoryUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<?> get(final String id) {
        return ResponseEntity.ok(CategoryApiPresenter
                .present(this.getCategoryByIdUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateCategoryApiInput input) {
        final var command = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.ok().body(output);

        return this.updateCategoryUseCase.execute(command).fold(onError, onSuccess);
    }


    @Override
    public Pagination<?> list(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        return null;
    }
}
