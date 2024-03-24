package com.codemagic.catalog.admin.application.category.update;

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

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIntegrationTest {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenShouldACategoryId() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));

        assertEquals(1, repository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;

        final var command = UpdateCategoryCommand.with(category.getId().getValue(), expectedName, expectedDescription, expectedActive);

        final var actualOutput = useCase.execute(command).get();
        final var actualCategory = assertDoesNotThrow(() -> this.repository.findById(actualOutput.id()).orElseThrow());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        assertEquals(expectedId.getValue(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedActive, actualCategory.isActive());
        assertEquals(category.getCreatedAt() , actualCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnANotification() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        assertEquals(0, repository.count());
        repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, repository.count());

        final String newCategoryName = null;
        final var newCategoryDescription = "The most watched movies";
        final var newCategoryActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), newCategoryName, newCategoryDescription, newCategoryActive);
        final var notification = useCase.execute(command).getLeft();

        assertEquals(1, this.repository.count());

        verify(gateway, times(1)).findById(eq(expectedId));

        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);

        verify(gateway, times(0)).update(any());

        final var actualCategory = assertDoesNotThrow(() -> this.repository.findById(expectedId.getValue()).orElseThrow());

        assertEquals(expectedId.getValue(), actualCategory.getId());

        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertEquals(category.isActive(), actualCategory.isActive());

        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategory_whenCallsUpdateInactiveCategory_thenShouldAInactivatedCategoryId() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        assertEquals(0, this.repository.count());
        repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, this.repository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = false;

        final var command = UpdateCategoryCommand.with(category.getId().getValue(), expectedName, expectedDescription, expectedActive);

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var actualOutput = useCase.execute(command).get();
        assertEquals(1, this.repository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway, times(1))
                .findById(eq(expectedId));

        final var actualCategory = assertDoesNotThrow(() -> this.repository.findById(expectedId.getValue()).orElseThrow());

        assertEquals(expectedId.getValue(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedActive, actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsARandomException_thenShouldReturnAException() {
        final var category = Category.newCategory("Mov", "The cat...");
        final var expectedId = category.getId();

        assertEquals(0, this.repository.count());
        this.repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, this.repository.count());

        final var newCategoryName = "Movies";
        final var newCategoryDescription = "The most watched movies";
        final var newCategoryActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), newCategoryName, newCategoryDescription, newCategoryActive);
        doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).update(any());

        final var notification = useCase.execute(command).getLeft();
        assertEquals(notification.first().message(), expectedErrorMessage);
        assertEquals(notification.getErrors().size(), expectedErrorCount);

        assertEquals(1, this.repository.count());
        final var actualCategory = assertDoesNotThrow(() -> this.repository.findById(expectedId.getValue()).orElseThrow());

        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertEquals(category.isActive(), actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInvalidCategoryId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {
        final var expectedId = "123";

        final var newCategoryName = "Movies";
        final var newCategoryDescription = "The most watched movies";
        final var newCategoryActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        assertEquals(0, this.repository.count());
        final var command = UpdateCategoryCommand.with(expectedId, newCategoryName, newCategoryDescription, newCategoryActive);
        assertEquals(0, this.repository.count());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(command));
        assertTrue(this.repository.findById(expectedId).isEmpty());

        assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorCount);

        verify(gateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        verify(gateway, times(0)).update(any());
    }

}
