package com.codemagic.catalog.admin.infrastructure.castmember;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CastMember create(final CastMember member) {
        return save(member);
    }

    @Override
    public CastMember update(CastMember member) {
        return null;
    }

    @Override
    public Optional<CastMember> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Pagination<CastMember> findAll(SearchQuery query) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    private CastMember save(final CastMember member) {
        return this.repository.save(CastMemberJpaEntity.from(member)).toAggregate();
    }
}
