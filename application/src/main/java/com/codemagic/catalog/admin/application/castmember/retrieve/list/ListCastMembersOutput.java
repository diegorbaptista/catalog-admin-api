package com.codemagic.catalog.admin.application.castmember.retrieve.list;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;

import java.time.Instant;

public record ListCastMembersOutput(String id, String name, CastMemberType type, Instant createdAt) {
    public static ListCastMembersOutput from(final CastMember member) {
        return new ListCastMembersOutput(member.getId().getValue(), member.getName(), member.getType(), member.getCreatedAt());
    }
}
