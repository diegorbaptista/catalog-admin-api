package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.ValueObject;

import java.util.Objects;

public final class AudioMediaVideo extends ValueObject {

    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private AudioMediaVideo(final String checksum,
                            final String name,
                            final String rawLocation,
                            final String encodedLocation,
                            final MediaStatus status) {
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioMediaVideo with(final String checksum,
                                       final String name,
                                       final String rawLocation,
                                       final String encodedLocation,
                                       final MediaStatus status) {
        return new AudioMediaVideo(checksum, name, rawLocation, encodedLocation, status);
    }

    public String checksum() {
        return checksum;
    }

    public String name() {
        return name;
    }

    public String rawLocation() {
        return rawLocation;
    }

    public String encodedLocation() {
        return encodedLocation;
    }

    public MediaStatus status() {
        return status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AudioMediaVideo that = (AudioMediaVideo) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation, that.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation);
    }
}
