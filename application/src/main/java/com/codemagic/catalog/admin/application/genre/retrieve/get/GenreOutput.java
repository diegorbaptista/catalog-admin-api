package com.codemagic.catalog.admin.application.genre.retrieve.get;

import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreOutput(String id,
                          String name,
                          boolean active,
                          Instant createdAt,
                          Instant updatedAt,
                          Instant deletedAt,
                          List<String> categories) {
    public static GenreOutput from(Genre genre) {
        return new GenreOutput(
                genre.getId().getValue(),
                genre.getName(),
                genre.isActive(),
                genre.getCreatedAt(),
                genre.getUpdatedAt(),
                genre.getDeletedAt(),
                genre.getCategories().stream().map(CategoryID::getValue).toList()
        );
    }
}
