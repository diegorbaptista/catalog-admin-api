package com.codemagic.catalog.admin.application.category.retrieve.list;

import com.codemagic.catalog.admin.application.UseCase;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
