package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {
    Video create(final Video video);
    Video update(final Video video);
    Optional<Video> findById(final VideoID videoID);
    Pagination<Video> findAll(final VideoSearchQuery query);
    void deleteById(final VideoID videoID);
}
