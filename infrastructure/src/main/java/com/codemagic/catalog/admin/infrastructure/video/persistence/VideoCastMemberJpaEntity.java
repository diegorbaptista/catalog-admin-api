package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
public class VideoCastMemberJpaEntity {

    @EmbeddedId
    private VideoCastMemberID id;

    @MapsId("videoId")
    @ManyToOne(fetch = FetchType.LAZY)
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity() {}

    private VideoCastMemberJpaEntity(final VideoJpaEntity video, final CastMemberID memberId) {
        this.id = VideoCastMemberID.from(video.getId(), memberId.getValue());
        this.video = video;
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity video, final CastMemberID memberId) {
        return new VideoCastMemberJpaEntity(video, memberId);
    }

    public VideoCastMemberID getId() {
        return id;
    }

    public void setId(VideoCastMemberID id) {
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
        final VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
