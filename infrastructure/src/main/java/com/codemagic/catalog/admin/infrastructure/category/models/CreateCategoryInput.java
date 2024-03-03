package com.codemagic.catalog.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description
) {
}
