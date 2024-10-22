package com.codemagic.catalog.admin.domain.video;

import java.util.Arrays;
import java.util.Optional;

public enum VideoResourceType {
    VIDEO,
    TRAILER,
    BANNER,
    THUMBNAIL,
    THUMBNAIL_HALF;

    public static Optional<VideoResourceType> of(final String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
