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

}
