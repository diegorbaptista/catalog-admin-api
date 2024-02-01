package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CategoryTest {
    @Test
    void givenAValidParams_whenCreatingANewCategory_thenCreateAValidCategory() {
        var expectedName = "Movies";
        var expectedDescription = "Most watched movies";
        var category = Category.newCategory(expectedName, expectedDescription);

        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertTrue(category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReturnAnError() {
        final String expectedName = null;
        final String expectedDescription = "Most watched movies";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription);
        final var expectedException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(expectedException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(expectedException.getErrors().size(), expectedErrorCount);
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReturnAnError() {
        final String expectedName = "    ";
        final String expectedDescription = "Most watched movies";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription);
        final var expectedException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(expectedException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(expectedException.getErrors().size(), expectedErrorCount);
    }
    @Test
    void givenAnInvalidNameGreaterThan255Characters_whenCallNewCategoryAndValidate_thenShouldReturnAnError() {
        final String expectedName = """
                t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.
                The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.
                Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.
                Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like.
                """;
        final String expectedDescription = "Most watched movies";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription);
        final var expectedException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(expectedException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(expectedException.getErrors().size(), expectedErrorCount);
    }

    @Test
    void givenAValidEmptyDescription_whenCreatingANewCategory_thenCreateAValidCategory() {
        var expectedName = "Movies";
        var expectedDescription = "   ";
        var category = Category.newCategory(expectedName, expectedDescription);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertTrue(category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactive() {
        var expectedName = "Movies";
        var expectedDescription = "Most watched movies";
        var category = Category.newCategory(expectedName, expectedDescription);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        final var updatedAt = category.getUpdatedAt();

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        var actualCategory = category.deactivate();

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertFalse(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActive() {
        var expectedName = "Movies";
        var expectedDescription = "Most watched movies";
        var category = Category.newCategory(expectedName, expectedDescription);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        var actualCategory = category.deactivate();

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertFalse(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());

        final var inactivatedAt = actualCategory.getUpdatedAt();
        final var reactivatedCategory = category.activate();

        assertEquals(reactivatedCategory.getId(), actualCategory.getId());
        assertEquals(reactivatedCategory.getName(), actualCategory.getName());
        assertEquals(reactivatedCategory.getDescription(), actualCategory.getDescription());
        assertTrue(reactivatedCategory.isActive());
        assertEquals(reactivatedCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(reactivatedCategory.getUpdatedAt().isAfter(inactivatedAt));
        assertNull(reactivatedCategory.getDeletedAt());
    }

}
