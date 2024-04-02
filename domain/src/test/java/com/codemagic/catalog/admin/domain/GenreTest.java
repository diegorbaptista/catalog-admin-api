package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    @Test
    void givenValidParams_whenCallsNewGenre_thenShouldReturnANewGenre() {
        final var expectedName = "Drama";
        final var expectedCategoriesCount = 0;
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenInvalidNullName_whenCallNewGenre_thenShouldReceiveAValidationError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var expectedException = assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenInvalidEmptyName_whenCallNewGenre_thenShouldReceiveAValidationError() {
        final String expectedName = "  ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var expectedException = assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenInvalidNameMinLength_whenCallNewGenre_thenShouldReceiveAValidationError() {
        final String expectedName = "zz";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var expectedException = assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenInvalidNameMaxLength_whenCallNewGenre_thenShouldReceiveAValidationError() {
        final String expectedName = """
                t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.
                The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.
                Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.
                Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like.
                """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var expectedException = assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenAValidGenre_whenDeactivateAGenre_thenShouldDeactivateAGenre() {
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategoriesCount = 0;
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenre_whenActivateADeactivatedGenre_thenShouldActivateAGenre() {
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategoriesCount = 0;
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var actualActivatedGenreUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.activate();

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(expectedCategoriesCount, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualActivatedGenreUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenre_whenCallUpdate_thenShouldUpdateAGenre() {
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedActive, expectedCategories);

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithDeactivate_thenShouldUpdateAGenre() {
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedActive, expectedCategories);

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithActivate_thenShouldUpdateAGenre() {
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        assertFalse(actualGenre.isActive());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var actualActivatedGenreUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.update(expectedName, expectedActive, expectedCategories);

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualActivatedGenreUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithInvalidNullName_thenShouldReturnANotificationException() {
        final String expectedName = null;
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var expectedException = assertThrows(NotificationException.class,
                () -> actualGenre.update(expectedName, expectedActive, expectedCategories));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithInvalidEmptyName_thenShouldReturnANotificationException() {
        final var expectedName = "  ";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var expectedException = assertThrows(NotificationException.class,
                () -> actualGenre.update(expectedName, expectedActive, expectedCategories));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithInvalidNameMinLength_thenShouldReturnANotificationException() {
        final var expectedName = "zz";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var expectedException = assertThrows(NotificationException.class,
                () -> actualGenre.update(expectedName, expectedActive, expectedCategories));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithInvalidNameMaxLength_thenShouldReturnANotificationException() {
        final var expectedName = """
                t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.
                The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.
                Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.
                Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like.
                """;
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var expectedCategories = List.of(CategoryID.from("1234"));
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var expectedException = assertThrows(NotificationException.class,
                () -> actualGenre.update(expectedName, expectedActive, expectedCategories));

        assertEquals(expectedErrorCount, expectedException.getErrors().size());
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithNullCategories_thenShouldUpdateAGenre() {
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var actualGenre = Genre.newGenre("...drama");

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedActive, null);

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithoutCategories_whenAddCategoriesID_thenShouldAddCategories() {
        final var moviesID = CategoryID.from("123");
        final var seriesID = CategoryID.from("456");
        final var expectedCategories = List.of(moviesID, seriesID);
        final var expectedName = "Drama";
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.addCategory(moviesID)
            .addCategory(seriesID);

        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    }

    @Test
    void givenAValidGenreWithoutCategories_whenAddNullCategoryID_thenShouldNotAddAnyCategoryID() {
        final var expectedName = "Drama";
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.addCategory(null);

        assertEquals(0, actualGenre.getCategories().size());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    }

    @Test
    void givenAValidGenreWithTwoCategories_whenRemoveCategoriesID_thenShouldRemoveCategoryID() {
        final var moviesID = CategoryID.from("123");
        final var seriesID = CategoryID.from("456");
        final var expectedCategories = List.of(moviesID);
        final var expectedName = "Drama";
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

        actualGenre.update(expectedName, true, List.of(moviesID, seriesID));
        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.removeCategory(seriesID);

        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    }

    @Test
    void givenAValidGenreWithTwoCategories_whenRemoveNullCategoryID_thenShouldNotRemoveAnyCategoryID() {
        final var moviesID = CategoryID.from("123");
        final var seriesID = CategoryID.from("456");
        final var expectedCategories = List.of(moviesID, seriesID);
        final var expectedName = "Drama";
        final var actualGenre = Genre.newGenre(expectedName);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertTrue(actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

        actualGenre.update(expectedName, true, expectedCategories);
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    }

}
