package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.genre.GenreID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
public class VideoGenreJpaEntity {

    @EmbeddedId
    private VideoGenreID id;

    @MapsId("videoId")
    @ManyToOne(fetch = FetchType.LAZY)
    private VideoJpaEntity video;

    public VideoGenreJpaEntity() {}

    private VideoGenreJpaEntity(final VideoJpaEntity video, final GenreID genreId) {
        this.id = VideoGenreID.from(video.getId(), genreId.getValue());
        this.video = video;
    }

    public static VideoGenreJpaEntity from(final VideoJpaEntity video, final GenreID genreId) {
        return new VideoGenreJpaEntity(video, genreId);
    }

    public VideoGenreID getId() {
        return id;
    }

    public void setId(VideoGenreID id) {
        this.id = id;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJpaEntity video) {
        this.video = video;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
