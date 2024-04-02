package com.codemagic.catalog.admin.application.category.retrieve.get;

import com.codemagic.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codemagic.catalog.admin.application.category.retrieve.get.DetaultGetCategoryByIdUseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {
    @InjectMocks
    private DetaultGetCategoryByIdUseCase useCase;
    @Mock
    private CategoryGateway gateway;
    @BeforeEach
    void cleanUp() {
        reset(gateway);
    }

    @Test
    void givenAValidCategoryId_whenCallsGetCategory_thenShouldReturnACategory () {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription);
        final var expectedId = category.getId();

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
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

        when(gateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

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

        when(gateway.findById(eq(expectedId))).thenThrow(new IllegalStateException(expectedErrorMessage));
        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId));
    }

}
