package com.codemagic.catalog.admin.infrastructure.castmember.presenters;

import com.codemagic.catalog.admin.application.castmember.retrieve.get.CastMemberOutput;
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
}
