package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.video.AudioMediaVideo;
import com.codemagic.catalog.admin.domain.video.MediaStatus;
import jakarta.persistence.*;

@Table(name = "videos_video_media")
@Entity(name = "AudioMediaVideo")
public class AudioMediaVideoJpaEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "local_path", nullable = false)
    private String localPath;

    @Column(name = "encoded_path")
    private String encodedPath;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public AudioMediaVideoJpaEntity() {}

    public AudioMediaVideoJpaEntity(
            final String id,
            final String name,
            final String localPath,
            final String encodedPath,
            final MediaStatus status) {
        this.id = id;
        this.name = name;
        this.localPath = localPath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioMediaVideoJpaEntity from(final AudioMediaVideo media) {
        return new AudioMediaVideoJpaEntity(
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.rawLocation(),
                media.status()
        );
    }

    public AudioMediaVideo toDomain() {
        return AudioMediaVideo.with(
                this.getId(),
                this.getName(),
                this.getLocalPath(),
                this.getEncodedPath(),
                this.getStatus()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }
}
