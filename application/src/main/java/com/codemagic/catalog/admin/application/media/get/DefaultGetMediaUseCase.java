package com.codemagic.catalog.admin.application.media.get;

import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.VideoID;
import com.codemagic.catalog.admin.domain.video.VideoResourceType;

import java.util.Objects;

public final class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway gateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand command) {
        final var id = VideoID.from(command.id());
        final var type = VideoResourceType.of(command.type())
                .orElseThrow(() -> notFound(command.type()));

        final var resource = this.gateway.getResource(id, type)
                .orElseThrow(() -> notFound(command.id(), command.type()));

        return MediaOutput.from(resource);
    }

    private NotFoundException notFound(final String type) {
        return NotFoundException.with(new Error("Media type '%s' does not exists".formatted(type)));
    }

    private NotFoundException notFound(final String id, final String type) {
        return NotFoundException.with(new Error("Media type '%s' for video %s was not found".formatted(type, id)));
    }
}
