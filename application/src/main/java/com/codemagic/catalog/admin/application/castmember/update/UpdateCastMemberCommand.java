package com.codemagic.catalog.admin.application.castmember.update;

import com.codemagic.catalog.admin.domain.castmember.CastMemberType;

public record UpdateCastMemberCommand(String id, String name, CastMemberType type) {
    public static UpdateCastMemberCommand with(final String id, final String name, final CastMemberType type) {
        return new UpdateCastMemberCommand(id, name, type);
    }
}
