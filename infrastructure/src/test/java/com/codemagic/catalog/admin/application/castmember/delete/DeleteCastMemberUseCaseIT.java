package com.codemagic.catalog.admin.application.castmember.delete;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
public class DeleteCastMemberUseCaseIT {

    @SpyBean
    private DeleteCastMemberUseCase useCase;

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
    void givenAValidMemberId_whenCallsDeleteMember_thenShouldDeleteAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = this.gateway.create(CastMember.newMember(expectedName, expectedType));;
        final var expectedId = member.getId().getValue();
        assertEquals(1, this.repository.count());

        // when
        assertDoesNotThrow(() -> this.gateway.deleteById(expectedId));

        // then
        assertEquals(0, this.repository.count());
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAmInvalidMemberId_whenCallsDeleteMember_thenShouldBeOk() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = this.gateway.create(CastMember.newMember(expectedName, expectedType));;
        final var expectedId = CastMemberID.from("123").getValue();
        assertEquals(1, this.repository.count());

        // when
        assertDoesNotThrow(() -> this.gateway.deleteById(expectedId));

        // then
        assertEquals(1, this.repository.count());
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

}
