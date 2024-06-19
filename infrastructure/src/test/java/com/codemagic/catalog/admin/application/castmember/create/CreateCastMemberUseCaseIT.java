package com.codemagic.catalog.admin.application.castmember.create;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @SpyBean
    private CreateCastMemberUseCase useCase;

    @SpyBean
    private CastMemberGateway gateway;

    @Autowired
    private CastMemberRepository repository;

    @Test
    void testDependencies() {
        assertNotNull(useCase);
        assertNotNull(gateway);
        assertNotNull(repository);
    }

    @Test
    void givenAValidCommand_whenCallsCreateMember_thenShouldCreateAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        // then
        final var actualMember = assertDoesNotThrow(() -> this.repository.findById(actualOutput.id()).orElseThrow());

        assertEquals(actualOutput.id(), actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());

        verify(gateway, times(1)).create(any());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final var expectedName = "  ";
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAInvalidMinNameLength_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final var expectedName = "A";
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAInvalidMaxNameLength_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final var expectedName = Fixture.lorem(300);
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAInvalidNullType_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(gateway, never()).create(any());
    }
}
