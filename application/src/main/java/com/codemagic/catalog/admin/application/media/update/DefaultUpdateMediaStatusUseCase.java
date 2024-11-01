package com.codemagic.catalog.admin.application.media.update;

import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.video.*;

import java.util.Objects;
import java.util.function.Supplier;

import static com.codemagic.catalog.admin.domain.video.VideoResourceType.TRAILER;
import static com.codemagic.catalog.admin.domain.video.VideoResourceType.VIDEO;

public final class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {

    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand command) {
        final var videoId = VideoID.from(command.videoId());
        final var status = command.status();
        final var resourceId = command.resourceId();
        final var folder = command.folder();
        final var filename = command.filename();
        final var encodedPath = "%s/%s".formatted(folder, filename);

        final var video = videoGateway.findById(videoId)
                .orElseThrow(notFound(videoId));

        if (matches(resourceId, video.getVideo().orElse(null))) {
            update(VIDEO, video, status, encodedPath);
            return;
        }

        if (matches(resourceId, video.getTrailer().orElse(null))) {
            update(TRAILER, video, status, encodedPath);
        }
    }

    private void update(final VideoResourceType type,
                        final Video video,
                        final MediaStatus status,
                        final String encodedPath) {
        switch (status) {
            case PENDING -> {}
            case PROCESSING -> video.processing(type);
            case COMPLETED -> video.completed(type, encodedPath);
        }
        videoGateway.update(video);
    }

    private boolean matches(final String resourceId, final AudioMediaVideo media) {
        if (media == null) return false;
        return media.id().equalsIgnoreCase(resourceId);
    }

    private Supplier<NotFoundException> notFound(final VideoID videoId) {
        return () -> NotFoundException.with(Video.class, videoId);
    }
}
