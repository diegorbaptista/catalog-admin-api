package com.codemagic.catalog.admin.infrastructure.category;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.category.CategorySearchQuery;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.codemagic.catalog.admin.infrastructure.util.SpecificationUtil.like;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    private Category save(final Category category) {
        return this.repository
                .save(CategoryJpaEntity.from(category))
                .toAggregate();
    }

    @Override
    public Category create(Category category) {
        return this.save(category);
    }

    @Override
    public Category update(Category category) {
        return this.save(category);
    }

    @Override
    public void deleteById(CategoryID categoryID) {
        final var id = categoryID.getValue();
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID categoryID) {
        return this.repository
                .findById(categoryID.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
                .filter(term -> !term.isBlank())
                .map(this::assembleFilter)
                .orElse(null);

        final var result = this.repository.findAll(where(where), page);

        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Specification<CategoryJpaEntity> assembleFilter(final String terms) {
        final Specification<CategoryJpaEntity> nameLike = like("name", terms);
        return nameLike.or(like("description", terms));
    }
}
