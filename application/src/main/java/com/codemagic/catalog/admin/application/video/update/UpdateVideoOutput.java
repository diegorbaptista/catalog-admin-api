package com.codemagic.catalog.admin.application.video.update;

import com.codemagic.catalog.admin.domain.video.Video;

public record UpdateVideoOutput(String id) {
    public static UpdateVideoOutput from(final Video video) {
        return new UpdateVideoOutput(video.getId().getValue());
    }
}
