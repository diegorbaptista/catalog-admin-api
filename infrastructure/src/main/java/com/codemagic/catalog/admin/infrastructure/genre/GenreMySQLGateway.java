package com.codemagic.catalog.admin.infrastructure.genre;

import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreJpaEntity;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import com.codemagic.catalog.admin.infrastructure.util.SpecificationUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository repository;

    public GenreMySQLGateway(final GenreRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Genre create(final Genre genre) {
        return save(genre);
    }

    @Override
    public Genre update(final Genre genre) {
        return save(genre);
    }

    @Override
    public Optional<Genre> findById(final GenreID id) {
        return this.repository
                .findById(id.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery query) {
       final var page = PageRequest.of(
               query.page(),
               query.perPage(),
               Sort.by(Sort.Direction.fromString(query.direction()),query.sort()));

        final var where = Optional.ofNullable(query.terms())
                .filter(term -> !term.isBlank())
                .map(this::assembleFilter)
                .orElse(null);


        final var result = this.repository.findAll(where(where), page);
        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public void deleteById(GenreID id) {
        this.repository.deleteById(id.getValue());
    }

    private Genre save(final Genre genre) {
        return this.repository.save(GenreJpaEntity.from(genre)).toAggregate();
    }

    private Specification<GenreJpaEntity> assembleFilter(final String terms) {
        return SpecificationUtil.like("name", terms);
    }

}
