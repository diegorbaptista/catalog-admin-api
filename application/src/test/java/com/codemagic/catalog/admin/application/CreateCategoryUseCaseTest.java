package com.codemagic.catalog.admin.application;

import com.codemagic.catalog.admin.application.category.create.CreateCategoryCommand;
import com.codemagic.catalog.admin.application.category.create.DefaultCreateCategoryUseCase;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldCreateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription);
        when(gateway.create(any())).thenAnswer(returnsFirstArg());
        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway, times(1))
                .create(argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(true, category.isActive()) && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription);
        final var notification = useCase.execute(command).getLeft();

        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);
        verify(gateway, times(0)).create(any());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsARandomException_thenShouldReturnAException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription);
        when(gateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();
        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);

        verify(gateway, times(1))
                .create(argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(true, category.isActive()) && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

}
