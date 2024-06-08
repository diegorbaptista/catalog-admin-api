package com.codemagic.catalog.admin.application.castmember.create;

import com.codemagic.catalog.admin.domain.castmember.CastMember;

public record CreateCastMemberOutput(String id) {
    public static CreateCastMemberOutput from(final CastMember member) {
        return new CreateCastMemberOutput(member.getId().getValue());
    }
}
