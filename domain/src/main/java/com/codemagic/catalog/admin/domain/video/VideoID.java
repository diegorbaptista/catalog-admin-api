package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    private final String value;

    private VideoID(final String id) {
        Objects.requireNonNull(id);
        this.value = id;
    }

    public static VideoID unique() {
        return from(UUID.randomUUID());
    }

    public static VideoID from(final String id) {
        return new VideoID(id);
    }

    public static VideoID from(final UUID id) {
        return new VideoID(id.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoID that = (VideoID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}
