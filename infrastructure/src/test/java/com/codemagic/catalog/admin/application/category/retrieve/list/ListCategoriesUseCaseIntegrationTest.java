package com.codemagic.catalog.admin.application.category.retrieve.list;

import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategorySearchQuery;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCategoriesUseCaseIntegrationTest {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    void givenAValidQuery_whenListCategories_thenShouldReturnACategoriesList() {
        final var categories = List.of(
                Category.newCategory("Movies", "The most watched movies"),
                Category.newCategory("Series", "The most watched series"));

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        assertEquals(0, this.repository.count());
        this.repository.saveAllAndFlush(categories.stream().map(CategoryJpaEntity::from).toList());
        assertEquals(2, this.repository.count());

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedItemCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        final var actualResult = useCase.execute(query);
        verify(gateway, times(1)).findAll(eq(query));

        assertEquals(expectedItemCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    void givenAValidQuery_whenListCategories_thenShouldReturnAEmptyCategoryList() {
        final var categories = List.<Category>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemCount = 0;

        assertEquals(0, this.repository.count());
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedItemCount, categories);
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        final var actualResult = useCase.execute(query);
        assertEquals(0, this.repository.count());

        verify(gateway, times(1)).findAll(eq(query));
        assertEquals(expectedItemCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedItemCount, actualResult.total());
    }

    @Test
    void givenAValidQuery_whenThrowsARandomException_thenShouldReturnAnException() {
        final var categories = List.of(
                Category.newCategory("Movies", "The most watched movies"),
                Category.newCategory("Series", "The most watched series"));

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway exception";

        assertEquals(0, this.repository.count());
        this.repository.saveAllAndFlush(categories.stream().map(CategoryJpaEntity::from).toList());
        assertEquals(2, this.repository.count());

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).findAll(query);
        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(gateway, times(1)).findAll(eq(query));
    }

}
