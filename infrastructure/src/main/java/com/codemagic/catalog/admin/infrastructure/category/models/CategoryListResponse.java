package com.codemagic.catalog.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
