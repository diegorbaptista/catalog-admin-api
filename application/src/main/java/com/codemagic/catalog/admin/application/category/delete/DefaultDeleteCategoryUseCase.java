package com.codemagic.catalog.admin.application.category.delete;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway gateway) {
        Objects.requireNonNull(gateway);
        this.gateway = gateway;
    }

    @Override
    public void execute(String categoryId) {
        this.gateway.deleteById(CategoryID.from(categoryId));
    }
}
