package com.codemagic.catalog.admin.domain.video;

public interface MediaResourceGateway {
    AudioMediaVideo storeAudioVideo(final VideoID id, final Resource resource);
    ImageMedia storeImage(final VideoID id, final Resource resource);
    void clear(final VideoID id);
}
