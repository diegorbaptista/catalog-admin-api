package com.codemagic.catalog.admin.application.genre.delete;

import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteGenreUseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway gateway;

    @Test
    void givenAValidGenreID_whenCallDeleteGenre_thenShouldDeleteAGenre() {
        final var genre = Genre.newGenre("Action");
        final var expectedId = genre.getId();

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidGenreID_whenCallDeleteGenre_thenShouldBeOK() {
        final var expectedId = GenreID.from("123");
        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidGenreID_whenCallDeleteGenreAndThrowAnError_thenShouldReceiveAnException() {

        final var genre = Genre.newGenre("Action");
        final var expectedId = genre.getId();

        doThrow(new IllegalStateException()).when(gateway).deleteById(any());

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(expectedId);
    }

}
