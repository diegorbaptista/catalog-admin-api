package com.codemagic.catalog.admin.application.castmember.retrieve.list;

import com.codemagic.catalog.admin.application.UseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<ListCastMembersOutput>>
        permits DefaultListCastMembersUseCase {
}
