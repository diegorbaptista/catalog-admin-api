package com.codemagic.catalog.admin.infrastructure.castmember.presenters;

import com.codemagic.catalog.admin.application.castmember.retrieve.get.CastMemberOutput;
import com.codemagic.catalog.admin.application.castmember.retrieve.list.ListCastMembersOutput;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberApiPresenter {
    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
                output.id(),
                output.name(),
                output.type(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static CastMemberListResponse present(final ListCastMembersOutput output) {
        return new CastMemberListResponse(
                output.id(),
                output.name(),
                output.type(),
                output.createdAt()
        );
    }
}
