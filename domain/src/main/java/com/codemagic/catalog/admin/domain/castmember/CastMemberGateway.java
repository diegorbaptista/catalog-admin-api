package com.codemagic.catalog.admin.domain.castmember;

import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(final CastMember member);
    CastMember update(final CastMember member);
    Optional<CastMember> findById(final String id);
    Pagination<CastMember> findAll(final SearchQuery query);
    void deleteById(final String id);
    List<CastMemberID> existsByIds(final Iterable<CastMemberID> members);
}
