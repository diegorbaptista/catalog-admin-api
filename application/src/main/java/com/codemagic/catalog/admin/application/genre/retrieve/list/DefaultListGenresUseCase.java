package com.codemagic.catalog.admin.application.genre.retrieve.list;

import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenresUseCase extends ListGenresUseCase {

    private final GenreGateway gateway;

    public DefaultListGenresUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery)
                .map(GenreListOutput::from);
    }
}
