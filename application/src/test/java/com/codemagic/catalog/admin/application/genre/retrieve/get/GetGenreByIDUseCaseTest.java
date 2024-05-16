package com.codemagic.catalog.admin.application.genre.retrieve.get;

import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetGenreByIDUseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIDUseCase useCase;

    @Mock
    private GenreGateway gateway;

    @Test
    void givenAValidGenreID_whenCallsGetGenreById_thenShouldReturnAGenre() {
        final var expectedGenre = Genre.newGenre("Action");
        final var expectedId = expectedGenre.getId();
        final var expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));
        expectedGenre.addCategories(expectedCategories);

        when(gateway.findById(any())).thenReturn(Optional.of(expectedGenre));

        final var actualGenre = assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedGenre.getName(), actualGenre.name());
        assertEquals(expectedGenre.isActive(), actualGenre.active());
        assertEquals(expectedGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(expectedGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(expectedGenre.getDeletedAt(), actualGenre.deletedAt());
        assertEquals(asString(expectedCategories), actualGenre.categories());

        verify(gateway, times(1)).findById(expectedId);
    }

    private List<String> asString(List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }

    @Test
    void givenAValidGenreID_whenCallsGetGenreAndNotFound_thenShouldReturnNotFound() {
        final var expectedId = GenreID.from("123").getValue();
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        when(gateway.findById(any())).thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(GenreID.from(expectedId));
    }

}
