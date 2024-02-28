package com.codemagic.catalog.admin.application.category.delete;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIntegrationTest {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCategoryID_whenCallsDeleteCategory_thenShouldBeOK() {
        var category = Category.newCategory("Movies", "The most watched movie");
        var expectedId = category.getId();

        assertEquals(0, repository.count());
        this.repository.save(CategoryJpaEntity.from(category));
        assertEquals(1, repository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(expectedId);
        assertEquals(0, repository.count());
    }

    @Test
    void givenAnInvalidCategoryID_whenClassDeleteCategory_thenShouldBeOK() {
        var expectedId = CategoryID.from("1234");
        assertEquals(0, repository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(expectedId);
        assertEquals(0, repository.count());
    }

    @Test
    void givenAValidCategoryID_whenClassDeleteCategory_thenShouldReturnAException() {
        var category = Category.newCategory("Movies", "The most watched movie");
        var expectedId = category.getId();
        var expectedErrorMessage = "Gateway error";

        assertEquals(0, repository.count());
        doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).deleteById(expectedId);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(expectedId);

        assertEquals(actualException.getMessage(), expectedErrorMessage);
        assertEquals(0, repository.count());
    }

}
