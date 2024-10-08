package com.codemagic.catalog.admin.domain.category;

import com.codemagic.catalog.admin.domain.Identifier;

import java.util.Objects;

import static com.codemagic.catalog.admin.domain.util.IdentifierUtil.*;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String id) {
        Objects.requireNonNull(id);
        this.value = id;
    }

    public static CategoryID unique() {
        return from(uuid());
    }

    public static CategoryID from(final String id) {
        return new CategoryID(id);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
