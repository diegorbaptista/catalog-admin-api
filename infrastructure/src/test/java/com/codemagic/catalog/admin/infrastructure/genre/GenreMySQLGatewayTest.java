package com.codemagic.catalog.admin.infrastructure.genre;

import com.codemagic.catalog.admin.MySQLGatewayTest;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreJpaEntity;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidGenre_whenCallsCreateGenre_thenShouldPersistAGenre() {
        final var movies = categoryGateway.create(
                Category.newCategory("Movies", "The most watched movies"));

        final var expectedName = "Action";
        final var expectedActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var genre = Genre.newGenre(expectedName);
        final var expectedId = genre.getId().getValue();
        genre.addCategories(expectedCategories);

        assertEquals(0, genreRepository.count());
        final var actualGenre = genreGateway.create(genre);
        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId).orElseThrow();
        assertEquals(expectedId, persistedGenre.getId());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoriesID());
        assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithoutCategories_whenCallsCreateGenre_thenShouldPersistAGenre() {
        final var expectedName = "Action";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName);
        final var expectedId = genre.getId().getValue();
        genre.addCategories(expectedCategories);

        assertEquals(0, genreRepository.count());
        final var actualGenre = genreGateway.create(genre);
        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId).orElseThrow();
        assertEquals(expectedId, persistedGenre.getId());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoriesID());
        assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_thenShouldUpdateAGenre() {
        final var movies = this.categoryGateway.create(
                Category.newCategory("Movies", "The most watched movies"));
        final var series = this.categoryGateway.create(
                Category.newCategory("Series", "The most watched series"));

        final var expectedName = "Action";
        final var expectedActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var genre = Genre.newGenre("Act...");
        final var expectedId = genre.getId().getValue();

        assertEquals(0, genreRepository.count());
        this.genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());
        assertEquals("Act...", genre.getName());
        assertEquals(0, genre.getCategories().size());

        final var actualGenre = genreGateway.update(Genre.with(genre)
                .update(expectedName, expectedActive, expectedCategories));
        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId).orElseThrow();
        assertEquals(expectedId, persistedGenre.getId());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedActive, persistedGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoriesID()));
        assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithCategories_whenCallsUpdateGenreRemovingCategories_thenShouldUpdateAGenre() {
        final var movies = this.categoryGateway.create(
                Category.newCategory("Movies", "The most watched movies"));
        final var series = this.categoryGateway.create(
                Category.newCategory("Series", "The most watched series"));

        final var expectedName = "Action";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("Act...");
        genre.addCategories(List.of(movies.getId(), series.getId()));
        final var expectedId = genre.getId().getValue();

        assertEquals(0, genreRepository.count());
        this.genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());
        assertEquals("Act...", genre.getName());
        assertEquals(2, genre.getCategories().size());

        final var actualGenre = genreGateway.update(Genre.with(genre)
                .update(expectedName, expectedActive, expectedCategories));
        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId).orElseThrow();
        assertEquals(expectedId, persistedGenre.getId());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoriesID());
        assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidActiveGenre_whenDeactivateAGenre_thenShouldUpdateToAInactiveGenre() {
        final var expectedName = "Action";
        final var expectedActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName);
        final var expectedId = genre.getId().getValue();

        assertEquals(0, genreRepository.count());
        this.genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());
        assertEquals(expectedName, genre.getName());

        final var actualGenre = genreGateway.update(Genre.with(genre).update(expectedName, expectedActive, expectedCategories));
        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId).orElseThrow();
        assertEquals(expectedId, persistedGenre.getId());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoriesID());
        assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNotNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidInactiveGenre_whenActivateAGenre_thenShouldUpdateToAnActiveGenre() {
        final var expectedName = "Action";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName);
        genre.deactivate();
        final var expectedId = genre.getId().getValue();

        assertEquals(0, genreRepository.count());
        this.genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());
        assertEquals(expectedName, genre.getName());
        assertFalse(genre.isActive());
        assertNotNull(genre.getDeletedAt());

        final var actualGenre = genreGateway.update(Genre.with(genre).update(expectedName, expectedActive, expectedCategories));
        assertEquals(1, genreRepository.count());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId).orElseThrow();
        assertEquals(expectedId, persistedGenre.getId());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoriesID());
        assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAPrePersistedGenreWithoutCategories_whenCallsDeleteGenre_thenShouldDeleteAGenre() {
        final var genre = Genre.newGenre("Action");
        assertEquals(0, this.genreRepository.count());

        this.genreRepository.save(GenreJpaEntity.from(genre));
        assertEquals(1, this.genreRepository.count());

        this.genreGateway.deleteById(genre.getId());
        assertEquals(0, this.genreRepository.count());
    }

    @Test
    void givenAPrePersistedGenreWithCategories_whenCallsDeleteGenre_thenShouldDeleteAGenre() {
        final var movies = this.categoryGateway.create(
                Category.newCategory("Movies", "The most watched movies"));
        final var series = this.categoryGateway.create(
                Category.newCategory("Series", "The most watched series"));
        final var genre = Genre.newGenre("Action");
        genre.addCategories(List.of(series.getId(), movies.getId()));
        assertEquals(0, this.genreRepository.count());

        this.genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, this.genreRepository.count());
        final var persistedGenre = this.genreRepository.findById(genre.getId().getValue());
        assertEquals(2, persistedGenre.orElseThrow().getCategories().size());

        this.genreGateway.deleteById(genre.getId());
        assertEquals(0, this.genreRepository.count());
    }

    @Test
    void givenNonExistentGenre_whenCallsDeleteGenre_thenShouldBeOK() {
        assertEquals(0, this.genreRepository.count());

        assertDoesNotThrow(() -> this.genreGateway.deleteById(GenreID.from("1234")));
        assertEquals(0, this.genreRepository.count());
    }

    @Test
    void givenAValidGenre_whenCallsGetGenreByID_thenShouldReturnAGenre() {
        final var movies = categoryGateway.create(
                Category.newCategory("Movies", "The most watched movies"));

        final var expectedName = "Action";
        final var expectedActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var genre = Genre.newGenre(expectedName);
        final var expectedId = genre.getId().getValue();
        genre.addCategories(expectedCategories);

        assertEquals(0, genreRepository.count());
        this.genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        final var actualGenre = assertDoesNotThrow(() -> this.genreGateway.findById(genre.getId()).orElseThrow());

        assertEquals(expectedId, actualGenre.getId().getValue());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenEmptyGenres_whenCallsFindAll_thenShouldReturnAEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = this.genreGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(0, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ac,0,10,1,1,Action",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Romantic Comedy",
            "fic,0,10,1,1,Scientific Fiction",
            "hor,0,10,1,1,Horror",
    })
    void givenAValidTerms_whenCallFindAll_thenShouldReturnPaginatedAndFilteredGenres(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = this.genreGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Action",
            "name,desc,0,10,5,5,Scientific Fiction",
            "createdAt,asc,0,10,5,5,Romantic Comedy",
            "createdAt,desc,0,10,5,5,Horror",
    })
    void givenAValidSortAndDirection_whenCallFindAll_thenShouldReturnPaginatedAndFilteredGenres(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        mockGenres();
        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = this.genreGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Action;Drama",
            "1,2,2,5,Horror;Romantic Comedy",
            "2,2,1,5,Scientific Fiction"
    })
    void givenAValidSortAndDirection_whenCallFindAll_thenShouldReturnPaginatedAndFilteredGenres(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenresNames
    ) {
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = this.genreGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for(final var expectedName: expectedGenresNames.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockGenres() {
        this.genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Romantic Comedy")),
                GenreJpaEntity.from(Genre.newGenre("Action")),
                GenreJpaEntity.from(Genre.newGenre("Drama")),
                GenreJpaEntity.from(Genre.newGenre("Scientific Fiction")),
                GenreJpaEntity.from(Genre.newGenre("Horror"))
        ));
    }

    @Test
    void givenAInvalidGenreID_whenCallsGetGenreByID_thenShouldReturnEmpty() {
        final var expectedId = GenreID.from("1234");

        assertEquals(0, genreRepository.count());

        final var actualGenre = this.genreGateway.findById(expectedId);
        assertTrue(actualGenre.isEmpty());
    }

    private List<CategoryID> sorted(final List<CategoryID> categories) {
        return categories.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }

}
