package com.codemagic.catalog.admin.application.castmember.update;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
public class UpdateCastMemberUseCaseIT {

    @SpyBean
    private UpdateCastMemberUseCase useCase;

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
    void givenAValidMember_whenCallsUpdate_thenShouldUpdateAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;
        final var actualMember = this.gateway.create(CastMember.newMember("Wrong name", CastMemberType.ACTOR));
        final var expectedId = actualMember.getId().getValue();

        assertEquals(1, this.repository.count());
        final var persistedMember = this.repository.findById(expectedId).orElseThrow();
        assertEquals(actualMember.getName(), persistedMember.getName());
        assertEquals(actualMember.getType(), persistedMember.getType());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));
        final var updatedMember = this.repository.findById(expectedId).orElseThrow();

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        assertEquals(expectedName, updatedMember.getName());
        assertEquals(expectedType, updatedMember.getType());
        assertEquals(updatedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertTrue(actualMember.getUpdatedAt().isBefore(updatedMember.getUpdatedAt()));

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(1)).update(argThat(member ->
            Objects.equals(expectedName, member.getName()) &&
                    Objects.equals(expectedType, member.getType()))
        );
    }

    @Test
    void givenAInvalidMemberId_whenCallsUpdate_thenShouldReturnNotFound() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;
        final var actualMember = this.gateway.create(CastMember.newMember(expectedName, expectedType));

        final var expectedId = CastMemberID.from("123").getValue();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedErrorCount = 1;

        assertEquals(1, this.repository.count());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(command));
        final var persistedMember = this.repository.findById(actualMember.getId().getValue()).orElseThrow();

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        assertEquals(persistedMember.getName(), actualMember.getName());
        assertEquals(persistedMember.getType(), actualMember.getType());
        assertEquals(persistedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(persistedMember.getUpdatedAt(), actualMember.getUpdatedAt());

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidNullName_whenCallsUpdate_thenShouldReturnNotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;
        final var actualMember = this.gateway.create(CastMember.newMember(Fixture.name(), expectedType));

        final var expectedId = actualMember.getId().getValue();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(1, this.repository.count());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));
        final var persistedMember = this.repository.findById(actualMember.getId().getValue()).orElseThrow();

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        assertEquals(persistedMember.getName(), actualMember.getName());
        assertEquals(persistedMember.getType(), actualMember.getType());
        assertEquals(persistedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(persistedMember.getUpdatedAt(), actualMember.getUpdatedAt());


        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsUpdate_thenShouldReturnNotificationException() {
        // given
        final var expectedName = "  ";
        final var expectedType = CastMemberType.DIRECTOR;
        final var actualMember = this.gateway.create(CastMember.newMember(Fixture.name(), expectedType));

        final var expectedId = actualMember.getId().getValue();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        assertEquals(1, this.repository.count());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));
        final var persistedMember = this.repository.findById(actualMember.getId().getValue()).orElseThrow();

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        assertEquals(persistedMember.getName(), actualMember.getName());
        assertEquals(persistedMember.getType(), actualMember.getType());
        assertEquals(persistedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(persistedMember.getUpdatedAt(), actualMember.getUpdatedAt());

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidMinLengthName_whenCallsUpdate_thenShouldReturnNotificationException() {
        // given
        final var expectedName = "A";
        final var expectedType = CastMemberType.DIRECTOR;
        final var actualMember = this.gateway.create(CastMember.newMember(Fixture.name(), expectedType));

        final var expectedId = actualMember.getId().getValue();
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        assertEquals(1, this.repository.count());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));
        final var persistedMember = this.repository.findById(actualMember.getId().getValue()).orElseThrow();

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        assertEquals(persistedMember.getName(), actualMember.getName());
        assertEquals(persistedMember.getType(), actualMember.getType());
        assertEquals(persistedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(persistedMember.getUpdatedAt(), actualMember.getUpdatedAt());

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidMaxLengthName_whenCallsUpdate_thenShouldReturnNotificationException() {
        // given
        final var expectedName = Fixture.lorem(300);
        final var expectedType = CastMemberType.DIRECTOR;
        final var actualMember = this.gateway.create(CastMember.newMember(Fixture.name(), expectedType));

        final var expectedId = actualMember.getId().getValue();
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        assertEquals(1, this.repository.count());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));
        final var persistedMember = this.repository.findById(actualMember.getId().getValue()).orElseThrow();

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        assertEquals(persistedMember.getName(), actualMember.getName());
        assertEquals(persistedMember.getType(), actualMember.getType());
        assertEquals(persistedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(persistedMember.getUpdatedAt(), actualMember.getUpdatedAt());

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidNullType_whenCallsUpdate_thenShouldReturnNotificationException() {
        // given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var actualMember = this.gateway.create(CastMember.newMember(expectedName, Fixture.CastMember.type()));

        final var expectedId = actualMember.getId().getValue();
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(1, this.repository.count());

        // when
        final var command = UpdateCastMemberCommand.with(expectedId, expectedName, expectedType);
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));
        final var persistedMember = this.repository.findById(actualMember.getId().getValue()).orElseThrow();

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        assertEquals(persistedMember.getName(), actualMember.getName());
        assertEquals(persistedMember.getType(), actualMember.getType());
        assertEquals(persistedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(persistedMember.getUpdatedAt(), actualMember.getUpdatedAt());

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

}
