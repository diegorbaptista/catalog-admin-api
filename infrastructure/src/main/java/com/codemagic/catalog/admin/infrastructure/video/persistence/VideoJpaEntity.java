package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoID;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {

    @Id
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @Column(name = "launch_year", nullable = false)
    private int launchedAt;

    @Column(name = "rating", nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private Double duration;

    @Column(name = "opened", nullable = false)
    private boolean opened;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    private AudioMediaVideoJpaEntity trailer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private AudioMediaVideoJpaEntity video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_id")
    private ImageMediaJpaEntity banner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private ImageMediaJpaEntity thumbnail;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_half_id")
    private ImageMediaJpaEntity thumbnailHalf;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCategoryJpaEntity> categories;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoGenreJpaEntity> genres;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCastMemberJpaEntity> members;

    public VideoJpaEntity() {}

    public VideoJpaEntity(final String id,
                          final String title,
                          final String description,
                          final int launchedAt,
                          final Rating rating,
                          final Double duration,
                          final boolean opened,
                          final boolean published,
                          final Instant createdAt,
                          final Instant updatedAt,
                          final AudioMediaVideoJpaEntity trailer,
                          final AudioMediaVideoJpaEntity video,
                          final ImageMediaJpaEntity banner,
                          final ImageMediaJpaEntity thumbnail,
                          final ImageMediaJpaEntity thumbnailHalf) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.rating = rating;
        this.duration = duration;
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.trailer = trailer;
        this.video = video;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = new HashSet<>();
        this.genres = new HashSet<>();
        this.members = new HashSet<>();
    }

    public static VideoJpaEntity from(final Video video) {
        final var entity = new VideoJpaEntity(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.getRating(),
                video.getDuration(),
                video.isOpened(),
                video.isPublished(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getTrailer().map(AudioMediaVideoJpaEntity::from).orElse(null),
                video.getVideo().map(AudioMediaVideoJpaEntity::from).orElse(null),
                video.getBanner().map(ImageMediaJpaEntity::from).orElse(null),
                video.getThumbnail().map(ImageMediaJpaEntity::from).orElse(null),
                video.getThumbnailHalf().map(ImageMediaJpaEntity::from).orElse(null));

        video.getCategories().forEach(entity::addCategory);
        video.getGenres().forEach(entity::addGenre);
        video.getCastMembers().forEach(entity::addCastMembers);
        return entity;
    }

    private void addCastMembers(final CastMemberID memberId) {
        this.members.add(VideoCastMemberJpaEntity.from(this, memberId));
    }

    private void addCategory(final CategoryID categoryId) {
        this.categories.add(VideoCategoryJpaEntity.from(this, categoryId));
    }

    private void addGenre(final GenreID genreId) {
        this.genres.add(VideoGenreJpaEntity.from(this, genreId));
    }

    public Video toDomain() {
        return Video.with(
                VideoID.from(this.getId()),
                this.getTitle(),
                this.getDescription(),
                Year.of(this.getLaunchedAt()),
                this.getDuration(),
                this.getRating(),
                this.isOpened(),
                this.isPublished(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                Optional.ofNullable(this.getBanner()).map(ImageMediaJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(this.getThumbnail()).map(ImageMediaJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(this.getThumbnailHalf()).map(ImageMediaJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(this.getTrailer()).map(AudioMediaVideoJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(this.getVideo()).map(AudioMediaVideoJpaEntity::toDomain).orElse(null),
                this.getCategories().stream()
                        .map(it -> CategoryID.from(it.getId().getCategoryId()))
                        .collect(Collectors.toSet()),
                this.getGenres().stream()
                        .map(it -> GenreID.from(it.getId().getGenreId()))
                        .collect(Collectors.toSet()),
                this.getMembers().stream()
                        .map(it -> CastMemberID.from(it.getId().getCastMemberId()))
                        .collect(Collectors.toSet())
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(int launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AudioMediaVideoJpaEntity getTrailer() {
        return trailer;
    }

    public void setTrailer(AudioMediaVideoJpaEntity trailer) {
        this.trailer = trailer;
    }

    public AudioMediaVideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(AudioMediaVideoJpaEntity video) {
        this.video = video;
    }

    public ImageMediaJpaEntity getBanner() {
        return banner;
    }

    public void setBanner(ImageMediaJpaEntity banner) {
        this.banner = banner;
    }

    public ImageMediaJpaEntity getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageMediaJpaEntity thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageMediaJpaEntity getThumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(ImageMediaJpaEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }

    public Set<VideoCategoryJpaEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<VideoCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public Set<VideoGenreJpaEntity> getGenres() {
        return genres;
    }

    public void setGenres(Set<VideoGenreJpaEntity> genres) {
        this.genres = genres;
    }

    public Set<VideoCastMemberJpaEntity> getMembers() {
        return members;
    }

    public void setMembers(Set<VideoCastMemberJpaEntity> members) {
        this.members = members;
    }
}
