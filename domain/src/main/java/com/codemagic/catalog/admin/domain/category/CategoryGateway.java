package com.codemagic.catalog.admin.domain.category;

import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(final Category category);
    void deleteById(final CategoryID categoryID);
    Optional<Category> findById(final CategoryID categoryID);
    Category update(final Category category);
    Pagination<Category> findAll(final SearchQuery query);
}
