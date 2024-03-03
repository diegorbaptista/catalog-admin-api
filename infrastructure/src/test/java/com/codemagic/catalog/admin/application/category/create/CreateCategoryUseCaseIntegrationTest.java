package com.codemagic.catalog.admin.application.category.create;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseIntegrationTest {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldCreateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";

        assertEquals(0, repository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription);
        final var actualOutput = useCase.execute(command).get();

        assertEquals(1, repository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = assertDoesNotThrow(() -> repository.findById(actualOutput.id()).orElseThrow());

        assertEquals(actualCategory.getId(), actualOutput.id());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(0, repository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription);
        final var notification = useCase.execute(command).getLeft();

        assertEquals(0, repository.count());
        verify(gateway, times(0)).create(any());

        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsARandomException_thenShouldReturnAException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        assertEquals(0, repository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription);
        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(gateway)
                    .create(any());

        final var notification = useCase.execute(command).getLeft();
        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);

        assertEquals(0, repository.count());
        verify(gateway, times(1)).create(any());
    }

}
