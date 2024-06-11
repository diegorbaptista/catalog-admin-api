package com.codemagic.catalog.admin.infrastructure.genre.presenters;

import com.codemagic.catalog.admin.application.genre.retrieve.get.GenreOutput;
import com.codemagic.catalog.admin.application.genre.retrieve.list.GenreListOutput;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {
    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.active(),
                output.categories(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt());
    }
    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.active(),
                output.createdAt(),
                output.categories()
        );
    }
}
