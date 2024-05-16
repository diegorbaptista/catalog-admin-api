package com.codemagic.catalog.admin.application.genre.retrieve.list;

import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListGenresUseCaseTest {

    @InjectMocks
    private DefaultListGenresUseCase useCase;

    @Mock
    private GenreGateway gateway;

    @Test
    void givenAValidTerms_whenCallsListGenres_thenShouldReturnGenresPaginated() {
        final var genres = List.of(
                Genre.newGenre("Action"),
                Genre.newGenre("Anime")
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ssc";
        final var expectedTotal = 2;

        final var expectedQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        when(gateway.findAll(any())).thenReturn(expectedPagination);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(expectedQuery));

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(expectedQuery));
    }

    @Test
    void givenAValidTerms_whenCallsListGenresAndResultIsEmpty_thenShouldReturnGenresPaginated() {
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ssc";
        final var expectedTotal = 0;

        final var expectedQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedItems = List.<GenreListOutput>of();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        when(gateway.findAll(any())).thenReturn(expectedPagination);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(expectedQuery));

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(expectedQuery));
    }

    @Test
    void givenAValidTerms_whenCallsListGenresAndThrowsAnError_thenShouldReturnAException() {
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ssc";
        final var expectedErrorMessage = "Gateway error";
        final var expectedQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(gateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findAll(eq(expectedQuery));
    }

}
