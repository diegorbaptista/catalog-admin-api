package com.codemagic.catalog.admin.infrastructure.castmember;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import com.codemagic.catalog.admin.infrastructure.util.SpecificationUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

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
        return save(member);
    }

    @Override
    public Optional<CastMember> findById(final String id) {
        return this.repository.findById(id)
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
                .filter(filter -> !filter.isBlank())
                .map(this::assembleFilter)
                .orElse(null);

        final var result = this.repository.findAll(where(where), page);

        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.stream()
                        .map(CastMemberJpaEntity::toAggregate)
                        .toList()
        );
    }

    @Override
    public void deleteById(final String id) {
        this.repository.deleteById(id);
    }

    private CastMember save(final CastMember member) {
        return this.repository.save(CastMemberJpaEntity.from(member)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleFilter(final String terms) {
        return SpecificationUtil.like("name", terms);
    }
}
