package com.codemagic.catalog.admin.application.genre.retrieve.list;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListGenresUseCaseIntegrationTest {

    @Autowired
    private ListGenresUseCase useCase;

    @Autowired
    private GenreGateway gateway;

    @Test
    void givenAValidTerms_whenCallsListGenres_thenShouldReturnGenresPaginated() {
        final var action = gateway.create(Genre.newGenre("Action"));
        final var anime = gateway.create(Genre.newGenre("Anime"));

        final var genres = List.of(action, anime);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();
        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(expectedQuery));

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
    }

    @Test
    void givenAValidTerms_whenCallsListGenresAndResultIsEmpty_thenShouldReturnGenresPaginated() {
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedItems = List.<GenreListOutput>of();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(expectedQuery));

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
    }

}
