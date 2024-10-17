package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.resource.Resource;

public interface MediaResourceGateway {
    AudioMediaVideo storeAudioVideo(final VideoID id, final VideoResource resource);
    ImageMedia storeImage(final VideoID id, final VideoResource resource);
    void clear(final VideoID id);
}
