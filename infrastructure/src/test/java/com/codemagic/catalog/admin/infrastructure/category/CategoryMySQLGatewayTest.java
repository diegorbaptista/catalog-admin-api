package com.codemagic.catalog.admin.infrastructure.category;

import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.MySQLGatewayTest;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway gateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    void givenAValidCategory_whenCallsCreate_thenShouldCreateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var category = Category.newCategory(expectedName, expectedDescription);

        assertEquals(0, repository.count());

        final var actualCategory = gateway.create(category);

        assertEquals(1, repository.count());
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(category.getId().getValue()).orElseThrow();

        assertEquals(category.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertTrue(actualEntity.isActive());
        assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_thenShouldUpdateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var category = Category.newCategory("Mov", null);

        assertEquals(0, repository.count());
        final var invalidEntity = this.repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, repository.count());

        assertEquals(category.getId().getValue(), invalidEntity.getId());
        assertEquals(category.getName(), invalidEntity.getName());
        assertEquals(category.getDescription(), invalidEntity.getDescription());
        assertTrue(invalidEntity.isActive());
        assertEquals(category.getCreatedAt(), invalidEntity.getCreatedAt());
        assertEquals(category.getUpdatedAt(), invalidEntity.getUpdatedAt());
        assertNull(invalidEntity.getDeletedAt());

        final var updatedCategory = category
                .clone()
                .update(expectedName, expectedDescription, true);

        final var actualCategory =  gateway.update(updatedCategory);
        assertEquals(1, repository.count());

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(category.getId().getValue()).orElseThrow();

        assertEquals(category.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertTrue(actualEntity.isActive());
        assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        assertEquals(category.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidPrePersistedCategoryId_whenDelete_thenShouldDeleteACategory() {
        final var category = Category.newCategory("Movies", null);

        assertEquals(0, this.repository.count());

        this.repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, this.repository.count());

        assertDoesNotThrow(() -> this.gateway.deleteById(category.getId()));
        assertEquals(0, this.repository.count());
    }

    @Test
    void givenAnInvalidCategoryId_whenDelete_thenShouldDeleteACategory() {
        assertEquals(0, this.repository.count());
        assertDoesNotThrow(() -> this.gateway.deleteById(CategoryID.from("invalid-id")));
        assertEquals(0, this.repository.count());
    }

    @Test
    void givenAValidPrePersistedCategory_whenGetById_thenShouldReturnACategory() {
        final var category = Category.newCategory("Movies", null);

        assertEquals(0, this.repository.count());

        this.repository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, this.repository.count());

        var actualCategory = assertDoesNotThrow(() -> this.gateway.findById(category.getId()).orElseThrow());

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidCategoryId_whenCallsFindById_thenShouldReturnEmpty() {
        assertEquals(0, this.repository.count());

        var actualCategory = this.gateway.findById(CategoryID.from("invalid-id"));

        assertEquals(0, this.repository.count());
        assertTrue(actualCategory.isEmpty());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAll_thenShouldReturnAPagination() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", "The most watched movies");
        final var series = Category.newCategory("Series", "The most viewed series");
        final var documentaries = Category.newCategory("Documentaries", "The most liked documentaries");

        assertEquals(0, this.repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");
        final var actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());

        final var actualCategory = actualResult.items().get(0);

        assertEquals(documentaries.getId(), actualCategory.getId());
        assertEquals(documentaries.getName(), actualCategory.getName());
        assertEquals(documentaries.getDescription(), actualCategory.getDescription());
        assertTrue(documentaries.isActive());
        assertEquals(documentaries.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(documentaries.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(documentaries.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(documentaries.getDeletedAt());
    }

    @Test
    void givenEmptyCategories_whenCallsFindAll_thenShouldReturnAEmptyPagination() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");
        final var actualResult = this.gateway.findAll(query);

        assertEquals(0, this.repository.count());

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
        assertTrue(actualResult.items().isEmpty());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAllAndFollowPagination_thenShouldPaginate() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", "The most watched movies");
        final var series = Category.newCategory("Series", "The most viewed series");
        final var documentaries = Category.newCategory("Documentaries", "The most liked documentaries");

        assertEquals(0, this.repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, this.repository.count());

        // Page <0>
        var query = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");
        var actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());

        var actualCategory = actualResult.items().get(0);

        assertEquals(documentaries.getId(), actualCategory.getId());
        assertEquals(documentaries.getName(), actualCategory.getName());
        assertEquals(documentaries.getDescription(), actualCategory.getDescription());
        assertTrue(documentaries.isActive());
        assertEquals(documentaries.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(documentaries.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(documentaries.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(documentaries.getDeletedAt());

        // Page <1>
        expectedPage = 1;
        query = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");
        actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());

        actualCategory = actualResult.items().get(0);

        assertEquals(movies.getId(), actualCategory.getId());
        assertEquals(movies.getName(), actualCategory.getName());
        assertEquals(movies.getDescription(), actualCategory.getDescription());
        assertTrue(movies.isActive());
        assertEquals(movies.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(movies.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(movies.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(movies.getDeletedAt());

        // Page <1>
        expectedPage = 2;
        query = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");
        actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());

        actualCategory = actualResult.items().get(0);

        assertEquals(series.getId(), actualCategory.getId());
        assertEquals(series.getName(), actualCategory.getName());
        assertEquals(series.getDescription(), actualCategory.getDescription());
        assertTrue(series.isActive());
        assertEquals(series.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(series.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(series.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(series.getDeletedAt());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAllWithFilterByNameAsDocumentaries_thenShouldReturnAValidCategoryPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", "The most watched movies");
        final var series = Category.newCategory("Series", "The most viewed series");
        final var documentaries = Category.newCategory("Documentaries", "The most liked documentaries");

        assertEquals(0, this.repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, "Doc", "name", "asc");
        final var actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());

        final var actualCategory = actualResult.items().get(0);

        assertEquals(documentaries.getId(), actualCategory.getId());
        assertEquals(documentaries.getName(), actualCategory.getName());
        assertEquals(documentaries.getDescription(), actualCategory.getDescription());
        assertTrue(documentaries.isActive());
        assertEquals(documentaries.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(documentaries.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(documentaries.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(documentaries.getDeletedAt());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAllWithFilterByDescriptionAsMostWatched_thenShouldReturnAValidMovieCategoryPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", "The most watched movies");
        final var series = Category.newCategory("Series", "The most viewed series");
        final var documentaries = Category.newCategory("Documentaries", "The most liked documentaries");

        assertEquals(0, this.repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, "MOST WATCHED", "name", "asc");
        final var actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());

        final var actualCategory = actualResult.items().get(0);

        assertEquals(movies.getId(), actualCategory.getId());
        assertEquals(movies.getName(), actualCategory.getName());
        assertEquals(movies.getDescription(), actualCategory.getDescription());
        assertTrue(movies.isActive());
        assertEquals(movies.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(movies.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(movies.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(movies.getDeletedAt());
    }

    @Test
    void givenPrePersistedCategories_whenCallsExistsByIDs_thenShouldReturnCategoryIds() {
        final var movies = Category.newCategory("Movies", "The most watched movies");
        final var series = Category.newCategory("Series", "The most viewed series");
        final var documentaries = Category.newCategory("Documentaries", "The most liked documentaries");

        assertEquals(0, this.repository.count());

        this.repository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, this.repository.count());

        final var ids = List.of(movies.getId(), series.getId(), CategoryID.from("123"));
        final var expectedIds = List.of(movies.getId(), series.getId());
        final var actualResult = this.gateway.existsByIds(ids);

        assertTrue(expectedIds.size() == actualResult.size() && expectedIds.containsAll(actualResult));
        assertEquals(2, actualResult.size());
    }

}
