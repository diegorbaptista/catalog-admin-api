package com.codemagic.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateGenreRequest(
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories
) {
}
