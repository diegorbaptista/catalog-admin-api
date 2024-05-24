package com.codemagic.catalog.admin.application.genre.retrieve.get;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class GetGenreByIDUseCaseIntegrationTest {

    @Autowired
    private GetGenreByIDUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidGenreID_whenCallsGetGenreById_thenShouldReturnAGenre() {
        final var movies = categoryGateway.create(Category.newCategory("Movies", null));
        final var series = categoryGateway.create(Category.newCategory("Series", null));

        final var expectedGenre = Genre.newGenre("Action");
        final var expectedId = expectedGenre.getId();
        final var expectedCategories = List.of(series.getId(), movies.getId());
        expectedGenre.addCategories(expectedCategories);
        genreGateway.create(expectedGenre);

        final var actualGenre = assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedGenre.getName(), actualGenre.name());
        assertEquals(expectedGenre.isActive(), actualGenre.active());
        assertEquals(expectedGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(expectedGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(expectedGenre.getDeletedAt(), actualGenre.deletedAt());
        assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && actualGenre.categories().containsAll(expectedCategories.stream().map(CategoryID::getValue).toList()));
    }

    @Test
    void givenAValidGenreID_whenCallsGetGenreAndNotFound_thenShouldReturnNotFound() {
        final var expectedId = GenreID.from("123").getValue();
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId));
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
