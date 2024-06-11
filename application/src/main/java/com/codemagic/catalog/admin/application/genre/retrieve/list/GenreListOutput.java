package com.codemagic.catalog.admin.application.genre.retrieve.list;

import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        String id,
        String name,
        boolean active,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {
    public static GenreListOutput from(Genre genre) {
        return new GenreListOutput(
                genre.getId().getValue(),
                genre.getName(),
                genre.isActive(),
                genre.getCategories().stream().map(CategoryID::getValue).toList(),
                genre.getCreatedAt(),
                genre.getDeletedAt()
        );
    }
}
