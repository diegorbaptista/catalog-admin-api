package com.codemagic.catalog.admin.infrastructure.castmember.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {
    Page<CastMemberJpaEntity> findAll(final Specification<CastMemberJpaEntity> where, final Pageable page);
}
