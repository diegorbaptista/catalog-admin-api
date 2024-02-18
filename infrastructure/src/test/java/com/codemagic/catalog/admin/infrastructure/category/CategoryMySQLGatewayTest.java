package com.codemagic.catalog.admin.infrastructure.category;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.infrastructure.MySQLGatewayTest;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway gateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    void givenAValidCategory_whenCallsCreate_thenShouldCreateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var category = Category.newCategory(expectedName, expectedDescription);

        assertEquals(0, repository.count());

        final var actualCategory = gateway.create(category);

        assertEquals(1, repository.count());
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(category.getId().getValue()).orElseThrow();

        assertEquals(category.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertTrue(actualEntity.isActive());
        assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

}
