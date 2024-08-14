package com.codemagic.catalog.admin.domain.genre;

import com.codemagic.catalog.admin.domain.Identifier;

import java.util.Objects;

import static com.codemagic.catalog.admin.domain.util.IdentifierUtil.uuid;

public class GenreID extends Identifier {

    private final String value;

    public GenreID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static GenreID unique() {
        return from(uuid());
    }

    public static GenreID from(final String id) {
        return new GenreID(id);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreID genreID = (GenreID) o;
        return Objects.equals(value, genreID.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
