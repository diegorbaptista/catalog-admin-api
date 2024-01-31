package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.domain.category.Category;
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

}
