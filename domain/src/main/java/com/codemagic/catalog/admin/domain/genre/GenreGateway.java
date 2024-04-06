package com.codemagic.catalog.admin.domain.genre;

import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
    Genre create(final Genre genre);
    Genre update(final Genre genre);
    Optional<Genre> findById(final GenreID id);
    Pagination<Genre> findAll(final SearchQuery query);
    void deleteById(final GenreID id);
}
