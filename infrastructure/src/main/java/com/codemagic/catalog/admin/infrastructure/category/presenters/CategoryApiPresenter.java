package com.codemagic.catalog.admin.infrastructure.category.presenters;

import com.codemagic.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codemagic.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.codemagic.catalog.admin.infrastructure.category.models.CategoryResponse;
import com.codemagic.catalog.admin.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

}
