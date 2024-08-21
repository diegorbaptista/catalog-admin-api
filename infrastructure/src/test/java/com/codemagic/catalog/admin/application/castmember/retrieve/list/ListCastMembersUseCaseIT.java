package com.codemagic.catalog.admin.application.castmember.retrieve.list;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
public class ListCastMembersUseCaseIT {

    @SpyBean
    private ListCastMembersUseCase useCase;

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
    void givenAValidTerms_whenCallsListMembers_thenShouldReturnMembers() {
        // given
        final var member1 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var genres = List.of(member1);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = member1.getName();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedItems = genres.stream().map(ListCastMembersOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(query));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedItemsCount, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(query));
    }

    @Test
    void givenAnEmptyList_whenCallsListMembers_thenShouldReturnEmptyPagination() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedItems = List.<ListCastMembersOutput>of();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(query));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedItemsCount, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(query));
    }

    @Test
    void givenAValidSortByCreatedAt_whenCallsListMembers_thenShouldReturnMembersSorted() {
        // given
        final var member1 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var member2 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var member3 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var genres = List.of(member1, member2, member3);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 3;
        final var expectedItems = genres.stream().map(ListCastMembersOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(query));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedItemsCount, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(query));
    }

    @Test
    void givenAValidSortByCreatedAtAndDirectionDesc_whenCallsListMembers_thenShouldReturnMembersSorted() {
        // given
        final var member1 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var member2 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var member3 = this.gateway.create(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var genres = List.of(member3, member2, member1);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 3;
        final var expectedItems = genres.stream().map(ListCastMembersOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(query));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedItemsCount, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(query));
    }

}
