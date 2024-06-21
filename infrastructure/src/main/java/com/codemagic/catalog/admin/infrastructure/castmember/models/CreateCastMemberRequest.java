package com.codemagic.catalog.admin.infrastructure.castmember.models;

import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCastMemberRequest(
        @JsonProperty("name") String name,
        @JsonProperty("type")  CastMemberType type) {
}
