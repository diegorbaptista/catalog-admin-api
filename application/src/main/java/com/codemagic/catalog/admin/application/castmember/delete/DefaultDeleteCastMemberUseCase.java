package com.codemagic.catalog.admin.application.castmember.delete;

import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;

import java.util.Objects;

public class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(id);
    }
}
