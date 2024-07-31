package com.codemagic.catalog.admin.application.video.retrieve.get;

import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway gateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public VideoOutput execute(final String videoId) {
        final var id = VideoID.from(videoId);
        return this.gateway.findById(id)
                .map(VideoOutput::from)
                .orElseThrow(notFound(id));
    }

    private Supplier<? extends RuntimeException> notFound(final VideoID identifier) {
        return () -> NotFoundException.with(Video.class, identifier);
    }
}
