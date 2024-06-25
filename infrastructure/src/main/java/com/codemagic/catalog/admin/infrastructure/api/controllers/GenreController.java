package com.codemagic.catalog.admin.infrastructure.api.controllers;

import com.codemagic.catalog.admin.application.genre.create.CreateGenreCommand;
import com.codemagic.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.codemagic.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.get.GetGenreByIDUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.list.ListGenresUseCase;
import com.codemagic.catalog.admin.application.genre.update.UpdateGenreCommand;
import com.codemagic.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.api.GenreAPI;
import com.codemagic.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.codemagic.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.codemagic.catalog.admin.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIDUseCase getGenreByIDUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenresUseCase listGenresUseCase;

    public GenreController(final CreateGenreUseCase createGenreUseCase,
                           final GetGenreByIDUseCase getGenreByIDUseCase,
                           final UpdateGenreUseCase updateGenreUseCase,
                           final DeleteGenreUseCase deleteGenreUseCase,
                           final ListGenresUseCase listGenresUseCase) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIDUseCase = Objects.requireNonNull(getGenreByIDUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.listGenresUseCase = Objects.requireNonNull(listGenresUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var command = CreateGenreCommand.with(input.name(), input.categories());
        final var output = this.createGenreUseCase.execute(command);
        return ResponseEntity
                .created(URI.create("/genres/".concat(output.id())))
                .body(output);
    }

    @Override
    public ResponseEntity<GenreResponse> get(final String id) {
        return ResponseEntity.ok(
                GenreApiPresenter.present(this.getGenreByIDUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateGenreRequest input) {
        final var command = UpdateGenreCommand.with(
                id,
                input.name(),
                input.active(),
                input.categories()
        );
        final var output = this.updateGenreUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public Pagination<GenreListResponse> list(int page, int perPage, String terms, String sort, String direction) {
        final var query = new SearchQuery(page, perPage, terms, sort, direction);
        return this.listGenresUseCase.execute(query)
                .map(GenreApiPresenter::present);
    }

    @Override
    public ResponseEntity<Void> delete(final String id) {
        this.deleteGenreUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
