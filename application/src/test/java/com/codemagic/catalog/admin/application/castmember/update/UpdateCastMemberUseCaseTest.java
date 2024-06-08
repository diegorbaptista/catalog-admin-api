package com.codemagic.catalog.admin.application.castmember.update;

import com.codemagic.catalog.admin.application.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCastMemberUseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Test
    void givenAValidCommand_whenCallUpdateCastMember_thenShouldUpdateAMember() {
        // given
        final var member = CastMember.newMember("Adam Sandler", CastMemberType.ACTOR);
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedId = member.getId();

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        when(gateway.findById(eq(expectedId.getValue()))).thenReturn(Optional.of(CastMember.with(member)));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.useCase.execute(command);

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(gateway, times(1)).findById(eq(expectedId.getValue()));

        verify(gateway, times(1)).update(argThat(updatedMember ->
                        Objects.equals(expectedId, updatedMember.getId())
                                && Objects.equals(expectedName, updatedMember.getName())
                                && Objects.equals(expectedType, updatedMember.getType())
                                && Objects.equals(member.getCreatedAt(), updatedMember.getCreatedAt())
                                && member.getUpdatedAt().isBefore(updatedMember.getUpdatedAt())
                )
        );
    }

    @Test
    void givenAnInvalidNullName_whenCallUpdateCastMember_thenShouldThrowANotificationException() {
        // given
        final var member = CastMember.newMember("Adam Sandler", CastMemberType.ACTOR);
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedId = member.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        when(gateway.findById(eq(expectedId.getValue()))).thenReturn(Optional.of(member));

        // when
        final var actualException = assertThrows(NotificationException.class,
                () -> this.useCase.execute(command));

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId.getValue()));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallUpdateCastMember_thenShouldThrowANotificationException() {
        // given
        final var member = CastMember.newMember("Adam Sandler", CastMemberType.ACTOR);
        final var expectedName = "  ";
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedId = member.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        when(gateway.findById(eq(expectedId.getValue()))).thenReturn(Optional.of(member));

        // when
        final var actualException = assertThrows(NotificationException.class,
                () -> this.useCase.execute(command));

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId.getValue()));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidNullType_whenCallUpdateCastMember_thenShouldThrowANotificationException() {
        // given
        final var member = CastMember.newMember("Adam Sandler", CastMemberType.ACTOR);
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedId = member.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        when(gateway.findById(eq(expectedId.getValue()))).thenReturn(Optional.of(member));

        // when
        final var actualException = assertThrows(NotificationException.class,
                () -> this.useCase.execute(command));

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId.getValue()));
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAnInvalidMemberId_whenCallUpdateCastMember_thenShouldThrowANotFoundException() {
        // given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        when(gateway.findById(eq(expectedId.getValue()))).thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class,
                () -> this.useCase.execute(command));

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId.getValue()));
        verify(gateway, never()).update(any());
    }

}
