package com.codemagic.catalog.admin.application.category.delete;

import com.codemagic.catalog.admin.application.category.delete.DefaultDeleteCategoryUseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        reset(gateway);
    }

    @Test
    void givenAValidCategoryID_whenCallsDeleteCategory_thenShouldBeOK() {
        var category = Category.newCategory("Movies", "The most watched movie");
        var expectedId = category.getId();

        doNothing().when(gateway).deleteById(eq(expectedId));
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidCategoryID_whenClassDeleteCategory_thenShouldBeOK() {
        var expectedId = CategoryID.from("1234");

        doNothing().when(gateway).deleteById(eq(expectedId));
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidCategoryID_whenClassDeleteCategory_thenShouldReturnAException() {
        var category = Category.newCategory("Movies", "The most watched movie");
        var expectedId = category.getId();
        var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).deleteById(expectedId);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(expectedId);

        assertEquals(actualException.getMessage(), expectedErrorMessage);
    }

}
