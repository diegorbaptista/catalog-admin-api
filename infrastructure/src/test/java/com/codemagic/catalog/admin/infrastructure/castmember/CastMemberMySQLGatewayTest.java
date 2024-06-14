package com.codemagic.catalog.admin.infrastructure.castmember;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.MySQLGatewayTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway gateway;

    @Autowired
    private CastMemberRepository repository;

    @Test
    void testDependencies() {
        assertNotNull(gateway);
        assertNotNull(repository);
    }

    @Test
    void givenAValidParams_whenCallsCreateMember_thenShouldCreateAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();
        assertEquals(0, repository.count());

        //when
        final var actualMember = this.gateway.create(CastMember.with(member));
        assertEquals(1, repository.count());

        // then
        assertNotNull(actualMember);
        assertEquals(expectedId, actualMember.getId().getValue());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, member.getType());
        assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(member.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = this.repository.findById(expectedId).orElseThrow();

        assertNotNull(persistedMember);
        assertEquals(expectedId, persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        assertEquals(member.getUpdatedAt(), persistedMember.getUpdatedAt());
    }


}