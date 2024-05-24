package com.codemagic.catalog.admin.application.genre.create;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
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
import static org.mockito.Mockito.*;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseIntegrationTest {

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private CreateGenreUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidCommandWithCategories_whenCallsCreateGenre_thenShouldCreateANewGenre() {
        final var moviesID = categoryGateway.create(Category.newCategory("Movies", null)).getId();
        final var seriesID = categoryGateway.create(Category.newCategory("Series", null)).getId();
        final var documentariesID = categoryGateway.create(Category.newCategory("Documentaries", null)).getId();

        final var expectedName = "Drama";
        final var expectedCategories = List.of(moviesID, seriesID, documentariesID);

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(actualOutput.id()).orElseThrow();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoriesID().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithoutCategories_whenCallsCreateGenre_thenShouldCreateANewGenre() {
        final var expectedName = "Drama";
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(actualOutput.id()).orElseThrow();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertTrue(actualGenre.getCategoriesID().isEmpty()
                && expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateGenre_thenShouldReturnANotificationException() {
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(null, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, never()).existsByIds(expectedCategories);
        verify(genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateGenre_thenShouldReturnANotificationException() {
        final var expectedName = "  ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, never()).existsByIds(expectedCategories);
        verify(genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidNameLength_whenCallsCreateGenre_thenShouldReturnANotificationException() {
        final var expectedName = "Dr";
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, never()).existsByIds(expectedCategories);
        verify(genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidNameBigLength_whenCallsCreateGenre_thenShouldReturnANotificationException() {
        final var expectedName = """
                t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.
                The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.
                Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.
                Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like.
                """;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, never()).existsByIds(expectedCategories);
        verify(genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnANotificationExceptionWithTwoErrors() {
        final var moviesID = categoryGateway.create(Category.newCategory("Movies", null)).getId();
        final var seriesID = categoryGateway.create(Category.newCategory("Series", null)).getId();

        final var expectedName = "  ";
        final var expectedErrorMessageOne = "Some categories could not be found: 1234";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;
        final var categories = List.of(moviesID, seriesID, CategoryID.from("1234"));

        final var command = CreateGenreCommand.with(expectedName, asString(categories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).existsByIds(categories);
        verify(genreGateway, never()).create(any());
    }


    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }


}
