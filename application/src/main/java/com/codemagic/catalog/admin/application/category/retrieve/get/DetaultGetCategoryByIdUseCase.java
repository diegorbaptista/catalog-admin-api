package com.codemagic.catalog.admin.application.category.retrieve.get;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DetaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private final CategoryGateway gateway;

    public DetaultGetCategoryByIdUseCase(final CategoryGateway gateway) {
        Objects.requireNonNull(gateway);
        this.gateway = gateway;
    }

    @Override
    public CategoryOutput execute(String categoryId) {
        return this.gateway
                .findById(CategoryID.from(categoryId))
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryId));
    }

    private static Supplier<DomainException> notFound(String categoryId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(categoryId)));
    }
}
