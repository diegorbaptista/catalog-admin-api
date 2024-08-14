package com.codemagic.catalog.admin.infrastructure.video.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoJpaRepository extends JpaRepository<VideoJpaEntity, String> {
}
