package com.codemagic.catalog.admin.infrastructure.castmember.models;

import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CastMemberListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") CastMemberType type,
        @JsonProperty("created_at") Instant createdAt) {
}
