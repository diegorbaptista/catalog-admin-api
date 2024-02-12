package com.codemagic.catalog.admin.application.retrieve.list;

import com.codemagic.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.codemagic.catalog.admin.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategorySearchQuery;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {
    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway gateway;
    @BeforeEach
    void cleanUp() {
        reset(gateway);
    }

    @Test
    void givenAValidQuery_whenListCategories_thenShouldReturnACategoriesList() {
        final var categories = List.of(Category.newCategory("Movies", "The most watched movies"),
                Category.newCategory("Series", "The most watched series"));

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedItemCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(gateway.findAll(eq(query))).thenReturn(expectedPagination);
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

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedItemCount, categories);
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(gateway.findAll(eq(query))).thenReturn(expectedPagination);
        final var actualResult = useCase.execute(query);

        verify(gateway, times(1)).findAll(eq(query));
        assertEquals(expectedItemCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedItemCount, actualResult.total());
    }

    @Test
    void givenAValidQuery_whenThrowsARandomException_thenShouldReturnAnException() {
        final var categories = List.of(Category.newCategory("Movies", "The most watched movies"),
                Category.newCategory("Series", "The most watched series"));

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway exception";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(gateway.findAll(eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));
        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(gateway, times(1)).findAll(eq(query));
    }

}
