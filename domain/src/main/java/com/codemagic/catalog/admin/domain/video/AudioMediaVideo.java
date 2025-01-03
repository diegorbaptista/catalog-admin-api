package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.ValueObject;
import com.codemagic.catalog.admin.domain.util.IdentifierUtil;

import java.util.Objects;
import java.util.UUID;

import static com.codemagic.catalog.admin.domain.video.MediaStatus.COMPLETED;
import static com.codemagic.catalog.admin.domain.video.MediaStatus.PROCESSING;

public final class AudioMediaVideo extends ValueObject {

    private final String id;
    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private AudioMediaVideo(final String id,
                            final String checksum,
                            final String name,
                            final String rawLocation,
                            final String encodedLocation,
                            final MediaStatus status) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioMediaVideo with(final String id,
                                       final String checksum,
                                       final String name,
                                       final String rawLocation,
                                       final String encodedLocation,
                                       final MediaStatus status) {
        return new AudioMediaVideo(id, checksum, name, rawLocation, encodedLocation, status);
    }

    public static AudioMediaVideo with(final String checksum,
                                       final String name,
                                       final String rawLocation,
                                       final String encodedLocation,
                                       final MediaStatus status) {
        return AudioMediaVideo.with(IdentifierUtil.uuid(), checksum, name, rawLocation, encodedLocation, status);
    }

    public static AudioMediaVideo with(final String checksum,
                                       final String name,
                                       final String rawLocation) {
        return AudioMediaVideo.with(checksum, name, rawLocation, "", MediaStatus.PENDING);
    }

    public String id() {
        return id;
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

    public AudioMediaVideo processing() {
        return AudioMediaVideo.with(
                id(),
                checksum(),
                name(),
                rawLocation(),
                encodedLocation(),
                PROCESSING
        );
    }

    public AudioMediaVideo completed(final String encodedPath) {
        return AudioMediaVideo.with(
                id(),
                checksum(),
                name(),
                rawLocation(),
                encodedPath,
                COMPLETED
        );
    }
}
