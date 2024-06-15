package com.codemagic.catalog.admin.infrastructure.castmember;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.MySQLGatewayTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void givenAValidParams_whenCallsUpdateMember_thenShouldUpdateAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember("Vin", CastMemberType.DIRECTOR);
        final var expectedId = member.getId().getValue();

        assertEquals(0, repository.count());
        final var createdMember = this.repository.saveAndFlush(CastMemberJpaEntity.from(member));

        assertEquals(1, repository.count());
        assertEquals(expectedId, createdMember.getId());
        assertEquals("Vin", createdMember.getName());
        assertEquals(CastMemberType.DIRECTOR, createdMember.getType());

        // when
        final var updatedMember = this.gateway.update(CastMember.with(member)
                .update(expectedName, expectedType));

        assertEquals(1, repository.count());

        // then
        assertNotNull(updatedMember);
        assertEquals(expectedId, updatedMember.getId().getValue());
        assertEquals(expectedName, updatedMember.getName());
        assertEquals(expectedType, updatedMember.getType());
        assertEquals(member.getCreatedAt(), updatedMember.getCreatedAt());
        assertTrue(member.getUpdatedAt().isBefore(updatedMember.getUpdatedAt()));

        final var persistedMember = this.repository.findById(expectedId).orElseThrow();

        assertNotNull(persistedMember);
        assertEquals(expectedId, persistedMember.getId());
        assertEquals(expectedName, persistedMember.getName());
        assertEquals(expectedType, persistedMember.getType());
        assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        assertTrue(member.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    void givenAValidMemberId_whenCallsDeleteMemberById_thenShouldDeleteAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();

        assertEquals(0, this.repository.count());
        this.repository.save(CastMemberJpaEntity.from(member));
        assertEquals(1, this.repository.count());

        // when
        this.gateway.deleteById(expectedId);

        // then
        assertEquals(0, this.repository.count());
    }

    @Test
    void givenAInvalidMemberId_whenCallsDeleteMemberById_thenShouldBeOK() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123").getValue();

        assertEquals(0, this.repository.count());
        this.repository.save(CastMemberJpaEntity.from(member));
        assertEquals(1, this.repository.count());

        // when
        this.gateway.deleteById(expectedId);

        // then
        assertEquals(1, this.repository.count());
    }

    @Test
    void givenAValidMember_whenCallsFindMemberById_thenShouldReturnAMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();

        assertEquals(0, this.repository.count());
        this.repository.save(CastMemberJpaEntity.from(member));
        assertEquals(1, this.repository.count());

        // when
        final var actualMember = this.gateway.findById(expectedId).orElseThrow();

        assertEquals(1, repository.count());

        // then
        assertNotNull(actualMember);
        assertEquals(expectedId, actualMember.getId().getValue());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
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

    @Test
    void givenAInvalidMemberId_whenCallsFindMemberById_thenShouldReturnEmpty() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123").getValue();

        assertEquals(0, this.repository.count());
        this.repository.save(CastMemberJpaEntity.from(member));
        assertEquals(1, this.repository.count());

        // when
        assertTrue(this.gateway.findById(expectedId).isEmpty());
        assertEquals(1, this.repository.count());
    }

    @Test
    void givenAEmptyCastMembers_whenCallsFindAll_thenShouldReturnAEmptyPagination() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.gateway.findAll(query);

        // then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedItemsCount, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "MAR,0,10,1,1,Martin Scorsese",
            "jas,0,10,1,1,Jason Statham",
            "harr,0,10,1,1,Kit Harrington"
    })
    void givenAValidTerm_whenCallsFindAll_thenShouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockMembers();
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, "name", "asc");

        // when
        final var actualPage = this.gateway.findAll(query);

        // then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Statham",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harrington",
            "createdAt,desc,0,10,5,5,Quentin Tarantino",
    })
    void givenAValidSortAndDirection_whenCallsFindAll_thenShouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockMembers();
        final var query = new SearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        // when
        final var actualPage = this.gateway.findAll(query);

        // then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Statham;Kit Harrington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel"
    })
    void givenAValidPagination_whenCallsFindAll_thenShouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNames
    ) {
        // given
        mockMembers();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var query = new SearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        // when
        final var actualPage = this.gateway.findAll(query);

        // then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        var index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }

    void mockMembers() {
        this.repository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newMember("Kit Harrington", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Jason Statham", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR))
        ));
    }


}