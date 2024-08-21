package com.codemagic.catalog.admin.application.castmember.retrieve.list;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCastMembersUseCaseTest {

    @InjectMocks
    private DefaultListCastMembersUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Test
    void givenAValidTerms_whenCallListCastMember_thenShouldReturnPaginatedMembers() {
        // given
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedItemsCount = 3;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItems = members.stream().map(ListCastMembersOutput::from).toList();
        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, expectedItemsCount, members);
        final var query = new SearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        when(gateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = this.useCase.execute(query);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedItemsCount, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(query));
    }

    @Test
    void givenAEmpty_whenCallListCastMember_thenShouldReturnEmptyPage() {
        // given
        final var members = List.<CastMember>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedItemsCount = 3;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItems = List.<ListCastMembersOutput>of();
        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerPage, expectedItemsCount, members);

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);
        when(gateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = this.useCase.execute(query);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedItemsCount, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway, times(1)).findAll(eq(query));
    }

    @Test
    void givenAValidTerms_whenCallListCastMemberAnGatewayThrowsException_thenShouldReceiveAException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);
        when(gateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var actualException = assertThrows(IllegalStateException.class,
                () -> this.useCase.execute(query));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findAll(eq(query));
    }

}
