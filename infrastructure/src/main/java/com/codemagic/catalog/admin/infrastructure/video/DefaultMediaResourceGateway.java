package com.codemagic.catalog.admin.infrastructure.video;

import com.codemagic.catalog.admin.domain.resource.Resource;
import com.codemagic.catalog.admin.domain.video.*;
import com.codemagic.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.codemagic.catalog.admin.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService service;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService service) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.service = service;
    }

    @Override
    public AudioMediaVideo storeAudioVideo(final VideoID id, final VideoResource videoResource) {
        final var filepath = filepath(id, videoResource.type());
        final var resource = videoResource.resource();
        store(filepath, resource);
        return AudioMediaVideo.with(resource.checksum(), resource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(VideoID id, VideoResource videoResource) {
        final var filepath = filepath(id, videoResource.type());
        final var resource = videoResource.resource();
        store(filepath, resource);
        return ImageMedia.with(resource.checksum(), resource.name(), filepath);
    }

    @Override
    public void clear(final VideoID id) {
        final var ids = this.service.list(folder(id));
        this.service.deleteAll(ids);
    }

    @Override
    public Optional<Resource> getResource(final VideoID id, final VideoResourceType type) {
        return this.service.get(filepath(id, type));
    }

    private void store(final String filepath, final Resource resource) {
        this.service.store(filepath,resource);
    }

    private String filename(final VideoResourceType type) {
        return filenamePattern.replace("{type}", type.name());
    }

    private String folder(final VideoID id) {
        return locationPattern.replace("{videoId}", id.getValue());
    }

    private String filepath(final VideoID id, final VideoResourceType type) {
        return folder(id)
                .concat("/")
                .concat(filename(type));
    }
}
