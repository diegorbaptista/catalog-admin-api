package com.codemagic.catalog.admin.application.genre.retrieve.get;

import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreByIDUseCase extends GetGenreByIDUseCase {

    private final GenreGateway gateway;

    public DefaultGetGenreByIDUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public GenreOutput execute(final String id) {
        final var genreID = GenreID.from(id);
        return this.gateway.findById(genreID)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, genreID));
    }
}
