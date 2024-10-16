package com.codemagic.catalog.admin.infrastructure.video;

import com.codemagic.catalog.admin.domain.Identifier;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.video.*;
import com.codemagic.catalog.admin.infrastructure.video.persistence.VideoJpaEntity;
import com.codemagic.catalog.admin.infrastructure.video.persistence.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.codemagic.catalog.admin.domain.util.CollectionUtil.mapTo;
import static com.codemagic.catalog.admin.domain.util.CollectionUtil.nullIfEmpty;
import static com.codemagic.catalog.admin.domain.util.SQLUtil.like;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository repository;

    public DefaultVideoGateway(final VideoRepository repository) {
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
    public Pagination<VideoPreview> findAll(final VideoSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var actualPage = this.repository.findAll(
                like(query.terms()),
                nullIfEmpty(mapTo(query.members(), Identifier::getValue)),
                nullIfEmpty(mapTo(query.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(query.genres(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
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
