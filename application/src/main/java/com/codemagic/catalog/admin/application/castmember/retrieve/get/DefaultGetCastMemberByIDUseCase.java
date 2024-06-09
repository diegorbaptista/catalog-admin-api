package com.codemagic.catalog.admin.application.castmember.retrieve.get;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;

import java.util.Objects;

public final class DefaultGetCastMemberByIDUseCase extends GetCastMemberByIDUseCase {

    private final CastMemberGateway gateway;

    public DefaultGetCastMemberByIDUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CastMemberOutput execute(final String id) {
        return CastMemberOutput.from(this.gateway.findById(id)
                .orElseThrow(() ->NotFoundException.with(CastMember.class, CastMemberID.from(id))));
    }
}
