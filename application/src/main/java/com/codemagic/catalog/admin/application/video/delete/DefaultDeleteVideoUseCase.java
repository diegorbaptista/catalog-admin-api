package com.codemagic.catalog.admin.application.video.delete;

import com.codemagic.catalog.admin.domain.exceptions.InternalErrorException;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String videoId) {
        final var id = VideoID.from(videoId);
        try {
            videoGateway.deleteById(id);
            mediaResourceGateway.clear(id);
        } catch (final Throwable throwable) {
            throw new InternalErrorException("An error was thrown deleting video: [%s]"
                    .formatted(videoId), throwable);
        }
    }
}
