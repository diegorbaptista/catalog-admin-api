package com.codemagic.catalog.admin.application.genre.update;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseIntegrationTest {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_thenShouldCreateANewGenre() {
        final var genre = genreGateway.create(Genre.newGenre("Dra"));
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                genre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(genre.getId().getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).orElseThrow();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertTrue(actualGenre.getCategories().isEmpty() &&
            expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(genre.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenreAndGenreIsInactive_thenShouldActivateAGenre() {
        final var genre = Genre.newGenre("Dra");
        genre.deactivate();
        assertFalse(genre.isActive());
        assertNotNull(genre.getDeletedAt());
        genreGateway.create(genre);

        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                genre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(genre.getId().getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).orElseThrow();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertTrue(actualGenre.getCategories().isEmpty() &&
                expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenreWithCategories_thenShouldCreateANewGenre() {
        final var genre = genreGateway.create(Genre.newGenre("Dra"));
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var movies = categoryGateway.create(Category.newCategory("Movies", null));
        final var expectedCategories = List.of(movies.getId());

        final var command = UpdateGenreCommand.with(
                genre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(genre.getId().getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).orElseThrow();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size() &&
                expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(genre.getDeletedAt());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsUpdateGenre_thenShouldReturnANotificationException() {
        final var genre = genreGateway.create(Genre.newGenre("Dra"));
        final var expectedName = "  ";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                genre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class,
                () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1))
                .findById(eq(genre.getId()));

        verify(genreGateway, never()).update(any());

        final var actualGenre = genreRepository.findById(genre.getId().getValue()).orElseThrow();

        assertEquals(genre.getName(), actualGenre.getName());
        assertEquals(genre.isActive(), actualGenre.isActive());
        assertTrue(actualGenre.getCategories().isEmpty() &&
                expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallsUpdateGenre_thenShouldReturnANotificationException() {
        final var genre = genreGateway.create(Genre.newGenre("Dra"));
        final String expectedName = null;
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                genre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class,
                () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1))
                .findById(eq(genre.getId()));

        verify(genreGateway, never()).update(any());

        final var actualGenre = genreRepository.findById(genre.getId().getValue()).orElseThrow();

        assertEquals(genre.getName(), actualGenre.getName());
        assertEquals(genre.isActive(), actualGenre.isActive());
        assertTrue(actualGenre.getCategories().isEmpty() &&
                expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnANotificationException() {
        final var movies = categoryGateway.create(Category.newCategory("Movies", null));
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");
        final var genre = genreGateway.create(Genre.newGenre("Dra"));

        final String expectedName = null;
        final var expectedActive = true;
        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var categories = List.of(movies.getId(), series, documentaries);

        final var command = UpdateGenreCommand.with(
                genre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(categories));

        final var actualException = assertThrows(NotificationException.class,
                () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        verify(genreGateway, times(1))
                .findById(eq(genre.getId()));

        verify(categoryGateway, times(1)).existsByIds(eq(categories));

        verify(genreGateway, never()).update(any());

        final var actualGenre = genreRepository.findById(genre.getId().getValue()).orElseThrow();

        assertEquals(genre.getName(), actualGenre.getName());
        assertEquals(genre.isActive(), actualGenre.isActive());
        assertTrue(actualGenre.getCategories().isEmpty() &&
                categories.containsAll(actualGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }


    private List<String> asString(final List<CategoryID> categories) {
        return categories
                .stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
