package com.codemagic.catalog.admin.application.video.create;

import com.codemagic.catalog.admin.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final Video video) {
        return new CreateVideoOutput(video.getId().getValue());
    }
}
