package com.codemagic.catalog.admin.application.castmember.create;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCastMemberUseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateMember_thenShouldReturnAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        when(gateway.create(any())).thenAnswer(returnsFirstArg());
        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(gateway, times(1)).create(argThat((castMember ->
                Objects.equals(expectedName, castMember.getName())
                        && Objects.equals(expectedType, castMember.getType())
                        && Objects.nonNull(castMember.getCreatedAt())
                        && Objects.nonNull(castMember.getUpdatedAt())))
        );
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final var expectedName = "  ";
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);
        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, never()).create(any());
    }

    @Test
    void givenAInvalidNullType_whenCallsCreateMember_thenShouldReturnANotificationException() {
        // given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(gateway, never()).create(any());
    }

}
