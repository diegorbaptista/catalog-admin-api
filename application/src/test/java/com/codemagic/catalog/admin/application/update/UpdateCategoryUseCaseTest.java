package com.codemagic.catalog.admin.application.update;

import com.codemagic.catalog.admin.application.category.create.CreateCategoryCommand;
import com.codemagic.catalog.admin.application.category.update.DefaultUpdateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryCommand;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenShouldACategoryId() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;

        final var command = UpdateCategoryCommand.with(category.getId().getValue(), expectedName, expectedDescription, expectedActive);

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway, times(1))
                .findById(eq(expectedId));

        verify(gateway, times(1))
                .update(argThat(updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedActive, updatedCategory.isActive())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnANotification() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        final String expectedName = null;
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);
        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(category));

        final var notification = useCase.execute(command).getLeft();

        verify(gateway, times(1)).findById(eq(expectedId));

        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);
        verify(gateway, times(0)).update(any());
    }

    @Test
    void givenAValidActiveCategory_whenCallsUpdateInactiveCategory_thenShouldAInactivatedCategoryId() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = false;

        final var command = UpdateCategoryCommand.with(category.getId().getValue(), expectedName, expectedDescription, expectedActive);

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway, times(1))
                .findById(eq(expectedId));

        verify(gateway, times(1))
                .update(argThat(updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedActive, updatedCategory.isActive())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.nonNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsARandomException_thenShouldReturnAException() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);
        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(gateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();
        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);

        verify(gateway, times(1)).findById(eq(expectedId));

        verify(gateway, times(1))
                .update(argThat(updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedActive, updatedCategory.isActive())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommandWithInvalidCategoryId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {
        final var expectedId = "123";

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedActive);
        when(gateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(command));
        assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorCount);

        verify(gateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        verify(gateway, times(0)).update(any());
    }


}
