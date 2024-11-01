package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.AggregateRoot;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.util.InstantUtil;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioMediaVideo trailer;
    private AudioMediaVideo video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    private Video(final VideoID id,
                  final String title,
                  final String description,
                  final Year launchedAt,
                  final double duration,
                  final Rating rating,
                  final boolean wasOpened,
                  final boolean wasPublished,
                  final Instant createdAt,
                  final Instant updatedAt,
                  final ImageMedia banner,
                  final ImageMedia thumbnail,
                  final ImageMedia thumbnailHalf,
                  final AudioMediaVideo trailer,
                  final AudioMediaVideo video,
                  final Set<CategoryID> categories,
                  final Set<GenreID> genres,
                  final Set<CastMemberID> members) {
        super(id);
        this.description = description;
        this.title = title;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.trailer = trailer;
        this.video = video;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = members;
    }

    public static Video newVideo(
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final Rating rating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members) {
        final var id = VideoID.unique();
        final var now = InstantUtil.now();
        return new Video(
                id,
                title,
                description,
                launchedAt,
                duration,
                rating,
                wasOpened,
                wasPublished,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members);
    }

    public static Video with(
            final VideoID id,
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final Rating rating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Instant createdAt,
            final Instant updatedAt,
            final ImageMedia banner,
            final ImageMedia thumbnail,
            final ImageMedia thumbnailHalf,
            final AudioMediaVideo trailer,
            final AudioMediaVideo video,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members) {
        return new Video(
                id,
                title,
                description,
                launchedAt,
                duration,
                rating,
                wasOpened,
                wasPublished,
                createdAt,
                updatedAt,
                banner,
                thumbnail,
                thumbnailHalf,
                trailer, video,
                categories,
                genres,
                members);
    }

    public static Video with(final Video video) {
        return new Video(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt(),
                video.getDuration(),
                video.getRating(),
                video.isOpened(),
                video.isPublished(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbnailHalf().orElse(null),
                video.getTrailer().orElse(null),
                video.getVideo().orElse(null),
                video.getCategories(),
                video.getGenres(),
                video.getCastMembers()
        );
    }

    public Video update(
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final Rating rating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members) {
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(members);
        this.updatedAt = InstantUtil.now();
        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Video setTrailer(final AudioMediaVideo trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Video setVideo(final AudioMediaVideo video) {
        this.video = video;
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtil.now();
        return this;
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
    }

    private void setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioMediaVideo> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioMediaVideo> getVideo() {
        return Optional.ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    public void processing(final VideoResourceType type) {
        if (type == VideoResourceType.VIDEO) {
            this.getVideo().ifPresent(media -> setVideo(media.processing()));
        } else if (type == VideoResourceType.TRAILER) {
            this.getTrailer().ifPresent(media -> setTrailer(media.processing()));
        }
    }

    public void completed(final VideoResourceType type, final String encodedPath) {
        if (type == VideoResourceType.VIDEO) {
            this.getVideo().ifPresent(media -> setVideo(media.completed(encodedPath)));
        } else if (type == VideoResourceType.TRAILER) {
            this.getTrailer().ifPresent(media -> setTrailer(media.completed(encodedPath)));
        }
    }
}
