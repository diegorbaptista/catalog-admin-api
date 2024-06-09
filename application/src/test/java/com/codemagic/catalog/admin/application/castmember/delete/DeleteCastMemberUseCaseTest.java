package com.codemagic.catalog.admin.application.castmember.delete;

import com.codemagic.catalog.admin.application.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCastMemberUseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Test
    void givenAValidMemberID_whenCallsDeleteCastMember_thenShouldBeOK() {
        // given
        final var member = CastMember.newMember(
                Fixture.name(),
                Fixture.CastMember.type()
        );
        final var expectedId = member.getId().getValue();
        doNothing().when(gateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAnInvalidMemberID_whenCallsDeleteCastMember_thenShouldBeOK() {
        final var expectedId = CastMemberID.from("123").getValue();
        doNothing().when(gateway).deleteById(expectedId);

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidMemberID_whenCallsDeleteCastMemberAndGatewayThrowsAnException_thenShouldThrowException() {
        final var expectedId = CastMemberID.from("123").getValue();

        doThrow(new IllegalStateException("Gateway error"))
                .when(gateway)
                .deleteById(any());

        // when
        assertThrows(IllegalStateException.class, () -> this.useCase.execute(expectedId));

        // then
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

}
