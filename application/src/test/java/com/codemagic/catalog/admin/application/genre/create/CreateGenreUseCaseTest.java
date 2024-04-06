package com.codemagic.catalog.admin.application.genre.create;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {
    @InjectMocks
    private DefaultCreateGenreUseCase useCase;
    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsCreateGenre_thenShouldCreateANewGenre() {
        final var expectedName = "Drama";
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));
        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(genreGateway, times(1))
                .create(argThat(genre ->
                        Objects.nonNull(genre.getId())
                                && Objects.equals(expectedName, genre.getName())
                                && Objects.equals(true, genre.isActive())
                                && Objects.equals(expectedCategories, genre.getCategories())
                                && Objects.nonNull(genre.getCreatedAt())
                                && Objects.nonNull(genre.getUpdatedAt())
                                && Objects.isNull(genre.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommandWithTwoCategories_whenCallsCreateGenre_thenShouldCreateANewGenre() {
        final var expectedName = "Drama";
        final var expectedCategories = List.<CategoryID>of(
                CategoryID.from("123"),
                CategoryID.from("456"));

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));
        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());
        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
                .existsByIds(expectedCategories);

        verify(genreGateway, times(1))
                .create(argThat(genre ->
                        Objects.nonNull(genre.getId())
                                && Objects.equals(expectedName, genre.getName())
                                && Objects.equals(true, genre.isActive())
                                && Objects.equals(expectedCategories, genre.getCategories())
                                && Objects.nonNull(genre.getCreatedAt())
                                && Objects.nonNull(genre.getUpdatedAt())
                                && Objects.isNull(genre.getDeletedAt())
                ));
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
    void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnANotificationException() {
        final var moviesID = CategoryID.from("123");
        final var seriesID = CategoryID.from("456");
        final var documentariesID = CategoryID.from("789");

        final var expectedName = "Drama";
        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;
        final var expectedCategories = List.of(moviesID, seriesID, documentariesID);

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(moviesID));

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnANotificationExceptionWithTwoErrors() {
        final var moviesID = CategoryID.from("123");
        final var seriesID = CategoryID.from("456");
        final var documentariesID = CategoryID.from("789");

        final var expectedName = "  ";
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;
        final var expectedCategories = List.of(moviesID, seriesID, documentariesID);

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(moviesID));

        final var command = CreateGenreCommand.with(expectedName, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, never()).create(any());
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
