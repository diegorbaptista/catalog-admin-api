package com.codemagic.catalog.admin.application.genre.delete;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class DeleteGenreUseCaseIntegrationTest {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidGenreID_whenCallDeleteGenre_thenShouldDeleteAGenre() {
        final var genre = Genre.newGenre("Action");
        final var expectedId = genre.getId();

        genreGateway.create(genre);

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, genreRepository.count());
    }

    @Test
    void givenAnInvalidGenreID_whenCallDeleteGenre_thenShouldBeOK() {
        final var genre = Genre.newGenre("Action");
        genreGateway.create(genre);

        final var expectedId = GenreID.from("123");

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(1, genreRepository.count());
    }

}



