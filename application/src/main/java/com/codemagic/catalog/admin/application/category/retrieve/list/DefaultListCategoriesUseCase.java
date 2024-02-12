package com.codemagic.catalog.admin.application.category.retrieve.list;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategorySearchQuery;
import com.codemagic.catalog.admin.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {
    private final CategoryGateway gateway;

    public DefaultListCategoriesUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery query) {
        return this.gateway.findAll(query)
                .map(CategoryListOutput::from);
    }
}
