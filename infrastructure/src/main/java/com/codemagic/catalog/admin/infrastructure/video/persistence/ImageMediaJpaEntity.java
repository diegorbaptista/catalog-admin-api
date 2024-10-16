package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.video.ImageMedia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "videos_image_media")
@Entity(name = "ImageMedia")
public class ImageMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "local_path", nullable = false)
    private String location;

    public ImageMediaJpaEntity() {}

    public ImageMediaJpaEntity(
            final String id,
            final String name,
            final String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static ImageMediaJpaEntity from(final ImageMedia media) {
        return new ImageMediaJpaEntity(
                media.checksum(),
                media.name(),
                media.location());
    }

    public ImageMedia toDomain() {
        return ImageMedia.with(
                this.getId(),
                this.getName(),
                this.getLocation());
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
