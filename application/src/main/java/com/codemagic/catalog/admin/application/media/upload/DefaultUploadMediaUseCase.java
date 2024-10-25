package com.codemagic.catalog.admin.application.media.upload;

import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.video.*;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultUploadMediaUseCase extends UploadMediaUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaGateway;

    public DefaultUploadMediaUseCase(final VideoGateway videoGateway,
                                     final MediaResourceGateway mediaGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaGateway = Objects.requireNonNull(mediaGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand command) {
        final var videoId = VideoID.from(command.videoId());
        final var type = command.videoResource().type();
        final var videoResource = command.videoResource();

        final var video = videoGateway.findById(videoId)
                .orElseThrow(notFound(videoId));

        switch (type) {
            case VIDEO -> video.setVideo(mediaGateway.storeAudioVideo(videoId, videoResource));
            case TRAILER -> video.setTrailer(mediaGateway.storeAudioVideo(videoId, videoResource));
            case BANNER -> video.setBanner(mediaGateway.storeImage(videoId, videoResource));
            case THUMBNAIL -> video.setThumbnail(mediaGateway.storeImage(videoId, videoResource));
            case THUMBNAIL_HALF -> video.setThumbnailHalf(mediaGateway.storeImage(videoId, videoResource));
        }

        this.videoGateway.update(video);

        return UploadMediaOutput.from(videoId, type);
    }

    private Supplier<NotFoundException> notFound(final VideoID id) {
        return () -> NotFoundException.with(Video.class, id);
    }

}
