package com.codemagic.catalog.admin.application.category.create;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }
    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();

        final var category = Category.newCategory(name, description);
        category.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(this.gateway.create(category));
    }
}
