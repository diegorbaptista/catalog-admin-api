package com.codemagic.catalog.admin.infrastructure.api.controllers;

import com.codemagic.catalog.admin.application.category.create.CreateCategoryCommand;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryOutput;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import com.codemagic.catalog.admin.infrastructure.api.CategoryAPI;
import com.codemagic.catalog.admin.infrastructure.category.models.CreateCategoryInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }
    @Override
    public ResponseEntity<?> create(CreateCategoryInput input) {
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
    public ResponseEntity<?> get(String id) {
        return null;
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
