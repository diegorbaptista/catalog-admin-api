package com.codemagic.catalog.admin.domain.castmember;

import com.codemagic.catalog.admin.domain.Identifier;

import java.util.Objects;

import static com.codemagic.catalog.admin.domain.util.IdentifierUtil.uuid;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CastMemberID unique() {
        return from(uuid());
    }

    public static CastMemberID from(final String id) {
        return new CastMemberID(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
