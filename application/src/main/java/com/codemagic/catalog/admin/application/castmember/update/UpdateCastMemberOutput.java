package com.codemagic.catalog.admin.application.castmember.update;

import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberOutput;
import com.codemagic.catalog.admin.domain.castmember.CastMember;

public record UpdateCastMemberOutput(String id) {
    public static UpdateCastMemberOutput from(final CastMember member) {
        return new UpdateCastMemberOutput(member.getId().getValue());
    }

    public static UpdateCastMemberOutput from(final String id) {
        return new UpdateCastMemberOutput(id);
    }
}
