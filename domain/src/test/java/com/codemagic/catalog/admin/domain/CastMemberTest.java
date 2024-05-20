package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CastMemberTest {

    @Test
    void givenAValidParams_whenCallsNewMember_thenShouldCreateANewMember() {
        final var expectedName = "Patrick J. Abrams";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember(expectedName, expectedType);

        assertNotNull(member);
        assertNotNull(member.getId());
        assertEquals(expectedName, member.getName());
        assertEquals(expectedType, member.getType());
        assertNotNull(member.getCreatedAt());
        assertNotNull(member.getUpdatedAt());
        assertEquals(member.getCreatedAt(), member.getUpdatedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallsNewMember_thenShouldReturnANotificationException() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsNewMember_thenShouldReturnANotificationException() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAnInvalidMinNameLength_whenCallsNewMember_thenShouldReturnANotificationException() {
        final var expectedName = "Hi";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var actualException = assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAnInvalidMaxNameLength_whenCallsNewMember_thenShouldReturnANotificationException() {
        final var expectedName = """
                t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.
                The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.
                Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy.
                Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' length must be between 3 and 255 characters";

        final var actualException = assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAnInvalidNullCastMemberType_whenCallsNewMember_thenShouldReturnANotificationException() {
        final var expectedName = "Gabriel Specter";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
