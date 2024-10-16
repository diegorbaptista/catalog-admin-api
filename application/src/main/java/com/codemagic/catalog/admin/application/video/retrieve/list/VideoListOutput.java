package com.codemagic.catalog.admin.application.video.retrieve.list;

import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(String id, String title, Instant createdAt) {
    public static VideoListOutput from(final Video video) {
        return new VideoListOutput(video.getId().getValue(), video.getTitle(), video.getCreatedAt());
    }
    public static VideoListOutput from(final VideoPreview video) {
        return new VideoListOutput(video.id(), video.title(), video.createdAt());
    }
}
