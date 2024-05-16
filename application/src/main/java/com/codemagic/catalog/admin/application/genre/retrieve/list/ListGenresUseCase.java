package com.codemagic.catalog.admin.application.genre.retrieve.list;

import com.codemagic.catalog.admin.application.UseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;

public abstract class ListGenresUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
