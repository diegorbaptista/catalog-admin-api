package com.codemagic.catalog.admin.application.media.get;

import com.codemagic.catalog.admin.domain.resource.Resource;

public record MediaOutput(
        String name,
        String contentType,
        byte[] content) {
    public static MediaOutput from(final Resource resource) {
        return new MediaOutput(resource.name(), resource.contentType(), resource.content());
    }
}
