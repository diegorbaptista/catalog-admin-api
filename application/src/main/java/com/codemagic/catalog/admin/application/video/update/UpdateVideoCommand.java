package com.codemagic.catalog.admin.application.video.update;

import com.codemagic.catalog.admin.domain.video.Resource;

import java.util.Optional;
import java.util.Set;

public record UpdateVideoCommand(
        String id,
        String title,
        String description,
        Integer launchedAt,
        String rating,
        Double duration,
        Boolean opened,
        Boolean published,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf) {

    public static UpdateVideoCommand with(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final String rating,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final Resource video,
            final Resource trailer,
            final Resource banner,
            final Resource thumbnail,
            final Resource thumbnailHalf) {
        return new UpdateVideoCommand(
                id,
                title,
                description,
                launchedAt,
                rating,
                duration,
                opened,
                published,
                categories,
                genres,
                members,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf);
    }

    public Optional<Integer> getLaunchedYear() {
        return Optional.ofNullable(launchedAt);
    }

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
}
