package com.codemagic.catalog.admin.application.video.create;

import com.codemagic.catalog.admin.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

public record CreateVideoCommand(
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        String rating,
        Boolean opened,
        Boolean published,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource trailer,
        Resource video,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf) {

    public static CreateVideoCommand with(
            final String title,
            final String description,
            final Integer launchedAt,
            final Double duration,
            final String rating,
            final Boolean opened,
            final Boolean published,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final Resource trailer,
            final Resource video,
            final Resource banner,
            final Resource thumbnail,
            final Resource thumbnailHalf) {
        return new CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                rating,
                opened,
                published,
                categories,
                genres,
                members,
                trailer,
                video,
                banner,
                thumbnail,
                thumbnailHalf
        );
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
