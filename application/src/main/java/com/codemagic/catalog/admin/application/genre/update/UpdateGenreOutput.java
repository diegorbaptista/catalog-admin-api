package com.codemagic.catalog.admin.application.genre.update;

import com.codemagic.catalog.admin.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
    public static UpdateGenreOutput from(final Genre genre) {
        return new UpdateGenreOutput(genre.getId().getValue());
    }
}
