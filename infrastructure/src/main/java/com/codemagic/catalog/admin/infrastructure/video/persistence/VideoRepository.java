package com.codemagic.catalog.admin.infrastructure.video.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {
}
