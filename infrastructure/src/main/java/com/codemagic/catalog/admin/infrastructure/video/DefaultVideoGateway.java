package com.codemagic.catalog.admin.infrastructure.video;

import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoID;
import com.codemagic.catalog.admin.domain.video.VideoSearchQuery;
import com.codemagic.catalog.admin.infrastructure.video.persistence.VideoJpaEntity;
import com.codemagic.catalog.admin.infrastructure.video.persistence.VideoJpaRepository;
import jakarta.transaction.Transactional;

import java.util.Objects;
import java.util.Optional;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoJpaRepository repository;

    public DefaultVideoGateway(final VideoJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    @Transactional
    public Video create(final Video video) {
        return save(video);
    }

    @Override
    @Transactional
    public Video update(final Video video) {
        return save(video);
    }

    @Override
    @Transactional
    public Optional<Video> findById(final VideoID videoId) {
        return this.repository
                .findById(videoId.getValue())
                .map(VideoJpaEntity::toDomain);
    }

    @Override
    @Transactional
    public Pagination<Video> findAll(final VideoSearchQuery query) {
        return null;
    }

    @Override
    @Transactional
    public void deleteById(final VideoID videoId) {
        this.repository.deleteById(videoId.getValue());
    }

    private Video save(final Video video) {
        return this.repository
                .save(VideoJpaEntity.from(video))
                .toDomain();
    }

}
