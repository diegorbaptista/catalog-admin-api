package com.codemagic.catalog.admin.application.genre.create;

import com.codemagic.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record CreateGenreOutput(String id) {
    public static CreateGenreOutput from(final String id) {
        return new CreateGenreOutput(id);
    }
    public static CreateGenreOutput from(final Genre genre) {
        return new CreateGenreOutput(genre.getId().getValue());
    }
}
