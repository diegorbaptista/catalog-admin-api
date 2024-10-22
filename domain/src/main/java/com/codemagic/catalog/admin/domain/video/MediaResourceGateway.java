package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.resource.Resource;

import java.util.Optional;

public interface MediaResourceGateway {
    AudioMediaVideo storeAudioVideo(final VideoID id, final VideoResource resource);
    ImageMedia storeImage(final VideoID id, final VideoResource resource);
    void clear(final VideoID id);
    Optional<Resource> getResource(final VideoID id, final VideoResourceType type);
}
