package com.codemagic.catalog.admin.application.video.retrieve.get;

import com.codemagic.catalog.admin.domain.Identifier;
import com.codemagic.catalog.admin.domain.util.CollectionUtil;
import com.codemagic.catalog.admin.domain.video.AudioMediaVideo;
import com.codemagic.catalog.admin.domain.video.ImageMedia;
import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Video;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
        String id,
        String title,
        String description,
        int launchedAt,
        Rating rating,
        double duration,
        boolean opened,
        boolean published,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        AudioMediaVideo video,
        AudioMediaVideo trailer,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        Instant createdAt,
        Instant updatedAt) {

    public static VideoOutput from(final Video video) {
        return new VideoOutput(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.getRating(),
                video.getDuration(),
                video.isOpened(),
                video.isPublished(),
                CollectionUtil.mapTo(video.getCategories(), Identifier::getValue),
                CollectionUtil.mapTo(video.getGenres(), Identifier::getValue),
                CollectionUtil.mapTo(video.getCastMembers(), Identifier::getValue),
                video.getVideo().orElse(null),
                video.getTrailer().orElse(null),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbnailHalf().orElse(null),
                video.getCreatedAt(),
                video.getUpdatedAt());
    }
}
