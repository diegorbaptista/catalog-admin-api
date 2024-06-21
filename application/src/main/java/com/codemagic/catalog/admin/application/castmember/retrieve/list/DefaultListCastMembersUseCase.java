package com.codemagic.catalog.admin.application.castmember.retrieve.list;

import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListCastMembersUseCase extends ListCastMembersUseCase {

    private final CastMemberGateway gateway;

    public DefaultListCastMembersUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ListCastMembersOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery)
                .map(ListCastMembersOutput::from);
    }
}
