package com.codemagic.catalog.admin.application.genre.update;

import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_thenShouldCreateANewGenre() {
        final var createdGenre = Genre.newGenre("Dra");
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                createdGenre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(createdGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(createdGenre.getId().getValue(), actualOutput.id());

        verify(genreGateway, times(1))
                .findById(eq(createdGenre.getId()));

        verify(genreGateway, times(1))
                .update(argThat(genre ->
                    Objects.equals(createdGenre.getId().getValue(), genre.getId().getValue())
                            && Objects.equals(expectedName, genre.getName())
                            && Objects.equals(expectedActive, genre.isActive())
                            && Objects.equals(expectedCategories, genre.getCategories())
                            && Objects.nonNull(genre.getCreatedAt())
                            && Objects.nonNull(genre.getUpdatedAt())
                            && createdGenre.getUpdatedAt().isBefore(genre.getUpdatedAt())
                            && Objects.nonNull(genre.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenreAndGenreIsInactive_thenShouldActivateAGenre() {
        final var createdGenre = Genre.newGenre("Dra");
        createdGenre.deactivate();
        assertFalse(createdGenre.isActive());
        assertNotNull(createdGenre.getDeletedAt());

        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                createdGenre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(createdGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(createdGenre.getId().getValue(), actualOutput.id());

        verify(genreGateway, times(1))
                .findById(eq(createdGenre.getId()));

        verify(genreGateway, times(1))
                .update(argThat(genre ->
                        Objects.equals(createdGenre.getId().getValue(), genre.getId().getValue())
                                && Objects.equals(expectedName, genre.getName())
                                && Objects.equals(expectedActive, genre.isActive())
                                && Objects.equals(expectedCategories, genre.getCategories())
                                && Objects.nonNull(genre.getCreatedAt())
                                && Objects.nonNull(genre.getUpdatedAt())
                                && createdGenre.getUpdatedAt().isBefore(genre.getUpdatedAt())
                                && Objects.isNull(genre.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenreWithCategories_thenShouldCreateANewGenre() {
        final var createdGenre = Genre.newGenre("Dra");
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var command = UpdateGenreCommand.with(
                createdGenre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(createdGenre)));
        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(createdGenre.getId().getValue(), actualOutput.id());

        verify(genreGateway, times(1))
                .findById(eq(createdGenre.getId()));

        verify(categoryGateway, times(1))
                .existsByIds(eq(expectedCategories));

        verify(genreGateway, times(1))
                .update(argThat(genre ->
                        Objects.equals(createdGenre.getId().getValue(), genre.getId().getValue())
                                && Objects.equals(expectedName, genre.getName())
                                && Objects.equals(expectedActive, genre.isActive())
                                && Objects.equals(expectedCategories, genre.getCategories())
                                && Objects.nonNull(genre.getCreatedAt())
                                && Objects.nonNull(genre.getUpdatedAt())
                                && createdGenre.getUpdatedAt().isBefore(genre.getUpdatedAt())
                                && Objects.nonNull(genre.getDeletedAt())
                ));
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsUpdateGenre_thenShouldReturnANotificationException() {
        final var createdGenre = Genre.newGenre("Dra");
        final var expectedName = "  ";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                createdGenre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(createdGenre)));

        final var actualException = assertThrows(NotificationException.class,
                () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1))
                .findById(eq(createdGenre.getId()));

        verify(genreGateway, never()).update(any());
    }

    @Test
    void givenAnInvalidNullName_whenCallsUpdateGenre_thenShouldReturnANotificationException() {
        final var createdGenre = Genre.newGenre("Dra");
        final String expectedName = null;
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                createdGenre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(createdGenre)));

        final var actualException = assertThrows(NotificationException.class,
                () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1))
                .findById(eq(createdGenre.getId()));

        verify(genreGateway, never()).update(any());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnANotificationException() {
        final var movies = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var createdGenre = Genre.newGenre("Dra");
        final String expectedName = null;
        final var expectedActive = true;
        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedCategories = List.of(movies, series, documentaries);

        final var command = UpdateGenreCommand.with(
                createdGenre.getId().getValue(),
                expectedName,
                expectedActive,
                asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(createdGenre)));
        when(categoryGateway.existsByIds(any())).thenReturn(List.of(movies));

        final var actualException = assertThrows(NotificationException.class,
                () -> useCase.execute(command));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        verify(genreGateway, times(1))
                .findById(eq(createdGenre.getId()));

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

        verify(genreGateway, never()).update(any());
    }


    private List<String> asString(final List<CategoryID> categories) {
        return categories
                .stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
