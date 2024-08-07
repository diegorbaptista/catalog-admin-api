package com.codemagic.catalog.admin.application.video.retrieve.list;

import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Video;

import java.time.Instant;

public record VideoListOutput(String id, String title, Rating rating, Instant createdAt) {
    public static VideoListOutput from(final Video video) {
        return new VideoListOutput(video.getId().getValue(), video.getTitle(), video.getRating(), video.getCreatedAt());
    }
}
