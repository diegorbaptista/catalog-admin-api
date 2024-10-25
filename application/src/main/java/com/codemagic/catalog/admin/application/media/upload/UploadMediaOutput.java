package com.codemagic.catalog.admin.application.media.upload;

import com.codemagic.catalog.admin.domain.video.VideoID;
import com.codemagic.catalog.admin.domain.video.VideoResourceType;

public record UploadMediaOutput(String id, VideoResourceType mediaType) {
    public static UploadMediaOutput from(final VideoID videoId, final VideoResourceType type) {
        return new UploadMediaOutput(videoId.getValue(), type);
    }
}
