package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.ValueObject;
import com.codemagic.catalog.admin.domain.resource.Resource;

import java.util.Objects;

public class VideoResource extends ValueObject {

    private final Resource resource;
    private final VideoResourceType type;

    public VideoResource(final Resource resource, final VideoResourceType type) {
        this.resource = Objects.requireNonNull(resource);
        this.type = Objects.requireNonNull(type);
    }

    public static VideoResource with(final Resource resource, final VideoResourceType type) {
        return new VideoResource(resource, type);
    }

    public Resource resource() {
        return resource;
    }

    public VideoResourceType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoResource that = (VideoResource) o;
        return Objects.equals(resource, that.resource) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, type);
    }
}
