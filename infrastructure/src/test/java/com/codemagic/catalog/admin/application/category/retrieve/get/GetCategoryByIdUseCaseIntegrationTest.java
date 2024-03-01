package com.codemagic.catalog.admin.application.category.retrieve.get;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetCategoryByIdUseCaseIntegrationTest {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCategoryId_whenCallsGetCategory_thenShouldReturnACategory () {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;

        assertEquals(0, this.repository.count());

        final var category = Category.newCategory(expectedName, expectedDescription);
        final var expectedId = category.getId();
        this.repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, this.repository.count());

        final var actualOutput = useCase.execute(expectedId.getValue());

        verify(gateway, times(1)).findById(eq(expectedId));

        assertEquals(CategoryOutput.from(category), actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedDescription, actualOutput.description());
        assertEquals(expectedActive, actualOutput.isActive());
        assertEquals(category.getCreatedAt(), actualOutput.createdAt());
        assertEquals(category.getUpdatedAt(), actualOutput.updatedAt());
        assertNull(actualOutput.deletedAt());
    }

    @Test
    void givenAnInvalidCategoryId_whenCallsGetCategory_thenShouldReturnNotFound() {
        final var expectedId = "1234";
        final var expectedErrorMessage = "Category with ID 1234 was not found";
        final var expectedErrorCount = 1;

        assertEquals(0, this.repository.count());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(expectedId));
        assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorCount);

        verify(gateway, times(1)).findById(eq(CategoryID.from(expectedId)));
    }

    @Test
    void givenAValidCommand_whenThrowsARandomException_thenReturnAException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "Gateway error";
        final var category = Category.newCategory(expectedName, expectedDescription);
        final var expectedId = category.getId();

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(gateway)
                .findById(expectedId);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId));
    }





}
