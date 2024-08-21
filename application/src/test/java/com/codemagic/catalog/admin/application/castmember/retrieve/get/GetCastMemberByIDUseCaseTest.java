package com.codemagic.catalog.admin.application.castmember.retrieve.get;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCastMemberByIDUseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberByIDUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Test
    void givenAValidCastMemberID_whenCallsGetByID_thenShouldReturnACastMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();

        when(gateway.findById(any())).thenReturn(Optional.of(CastMember.with(member)));

        // when
        final var actualOutput = this.useCase.execute(expectedId);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId, actualOutput.id());
        assertEquals(expectedName, actualOutput.name());
        assertEquals(expectedType, actualOutput.type());
        assertEquals(member.getCreatedAt(), actualOutput.createdAt());
        assertEquals(member.getUpdatedAt(), actualOutput.updatedAt());

        verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAnInvalidCastMemberID_whenCallsGetByID_thenShouldReturnNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("123").getValue();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedErrorCount = 1;

        when(gateway.findById(any())).thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class,
                () -> this.useCase.execute(expectedId));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId));
    }

}
