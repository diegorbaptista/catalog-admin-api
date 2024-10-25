package com.codemagic.catalog.admin.application.media.upload;

import com.codemagic.catalog.admin.domain.video.VideoResource;

public record UploadMediaCommand(String videoId, VideoResource videoResource) {
    public static UploadMediaCommand with(final String videoId, final VideoResource videoResource) {
        return new UploadMediaCommand(videoId, videoResource);
    }
}
