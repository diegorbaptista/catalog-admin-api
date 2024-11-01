package com.codemagic.catalog.admin.application.media.update;

import com.codemagic.catalog.admin.domain.video.MediaStatus;

public record UpdateMediaStatusCommand(
        String videoId,
        String resourceId,
        MediaStatus status,
        String folder,
        String filename) {

    public static UpdateMediaStatusCommand with(
            final String videoId,
            final String resourceId,
            final MediaStatus status,
            final String folder,
            final String filename) {
        return new UpdateMediaStatusCommand(videoId, resourceId, status, folder, filename);
    }
}
