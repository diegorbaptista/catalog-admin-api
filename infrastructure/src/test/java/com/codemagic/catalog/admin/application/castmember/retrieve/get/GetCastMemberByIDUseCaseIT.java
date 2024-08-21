package com.codemagic.catalog.admin.application.castmember.retrieve.get;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
public class GetCastMemberByIDUseCaseIT {

    @SpyBean
    private GetCastMemberByIDUseCase useCase;

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
    void givenAValidMemberId_whenCallGetById_thenShouldReturnAMember() {
       // given
       final var expectedName = Fixture.name();
       final var expectedType = Fixture.CastMembers.type();

       assertEquals(0, this.repository.count());
       final var actualMember = this.gateway.create(CastMember.newMember(expectedName, expectedType));
       final var expectedId = actualMember.getId().getValue();
       assertEquals(1, this.repository.count());

       // when
       final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(expectedId));

       // then
       assertNotNull(actualOutput);
       assertEquals(expectedName, actualOutput.name());
       assertEquals(expectedType, actualOutput.type());
       assertEquals(actualMember.getCreatedAt(), actualOutput.createdAt());
       assertEquals(actualMember.getUpdatedAt(), actualOutput.updatedAt());
    }

    @Test
    void givenAnInvalidMemberId_whenCallsGeyById_thenShouldReturnANotFoundException() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var actualMember = this.gateway.create(CastMember.newMember(expectedName, expectedType));
        final var expectedId = CastMemberID.from("123").getValue();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        assertEquals(1, this.repository.count());

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedId));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

}
