package com.codemagic.catalog.admin.infrastructure.category.persistence;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void givenANullCategoryName_whenCallsCreate_shouldReturnAnError() {
        final var expectedMessage = "not-null property references a null or transient value : com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var expectedPropertyName = "name";
        final var category = Category.newCategory("Movies", "The most watched movies");

        var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        var actualException = assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(entity));
        var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedMessage, actualCause.getMessage());
        assertEquals(expectedPropertyName, actualCause.getPropertyName());
    }

    @Test
    void givenANullCategoryCreatedAt_whenCallsCreate_shouldReturnAnError() {
        final var expectedMessage = "not-null property references a null or transient value : com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var expectedPropertyName = "createdAt";
        final var category = Category.newCategory("Movies", "The most watched movies");

        var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        var actualException = assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(entity));
        var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedMessage, actualCause.getMessage());
        assertEquals(expectedPropertyName, actualCause.getPropertyName());
    }

    @Test
    void givenANullCategoryUpdatedAt_whenCallsCreate_shouldReturnAnError() {
        final var expectedMessage = "not-null property references a null or transient value : com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var expectedPropertyName = "updatedAt";
        final var category = Category.newCategory("Movies", "The most watched movies");

        var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        var actualException = assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(entity));
        var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedMessage, actualCause.getMessage());
        assertEquals(expectedPropertyName, actualCause.getPropertyName());
    }

}
