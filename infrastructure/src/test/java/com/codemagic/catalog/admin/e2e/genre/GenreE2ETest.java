package com.codemagic.catalog.admin.e2e.genre;

import com.codemagic.catalog.admin.E2ETest;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.e2e.MockDsl;
import com.codemagic.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.codemagic.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository repository;

    @Autowired
    private GenreGateway gateway;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("adm_catalog_db");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        final var expectedName = "Horror";
        final var expectedCategories = List.<CategoryID>of();

        assertEquals(0, repository.count());
        final var actualGenreId = givenAGenre(expectedName, expectedCategories);
        assertEquals(1, repository.count());

        final var actualGenre = this.repository.findById(actualGenreId.getValue()).orElseThrow();

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertTrue(actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }


    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        final var movies = givenACategory("Movies", "The most watched movies");
        final var expectedName = "Comedy";
        final var expectedCategories = List.of(movies);

        assertEquals(0, repository.count());

        final var actualGenreId = givenAGenre(expectedName, expectedCategories);
        final var actualGenre = assertDoesNotThrow(() -> repository.findById(actualGenreId.getValue()).orElseThrow());

        assertEquals(1, repository.count());

        assertEquals(expectedName, actualGenre.getName());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesID()));
        assertTrue(actualGenre.isActive());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateAnExistingGenreWithItsCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        final var movies = givenACategory("Movies", "The most watched movies");
        final var series = givenACategory("Series", "The most watched series");
        final var documentaries = givenACategory("Documentaries", "The most watched documentaries");
        final var expectedName = "Drama";
        final var expectedActive = false;
        final var expectedCategories = List.of(movies, series);

        final var expectedCreatedName = "Wrong name";
        final var expectedCreatedCategories = List.of(movies, series, documentaries);

        assertEquals(0, repository.count());
        final var actualGenreId = givenAGenre(expectedCreatedName, expectedCreatedCategories);
        assertEquals(1, repository.count());

        final var createdGenre = assertDoesNotThrow(() -> repository.findById(actualGenreId.getValue()).orElseThrow());

        assertEquals(expectedCreatedName, createdGenre.getName());
        assertTrue(expectedCreatedCategories.size() == createdGenre.getCategories().size()
                && expectedCreatedCategories.containsAll(createdGenre.getCategoriesID()));
        assertTrue(createdGenre.isActive());
        assertNotNull(createdGenre.getCreatedAt());
        assertNotNull(createdGenre.getUpdatedAt());
        assertNull(createdGenre.getDeletedAt());

        final var requestBody = new UpdateGenreRequest(expectedName, expectedActive,
                expectedCategories.stream().map(CategoryID::getValue).toList());

        updateAGenre(actualGenreId.getValue(), requestBody)
                .andExpect(status().isOk());

        final var actualGenre = retrieveAGenre(actualGenreId.getValue());

        assertEquals(expectedName, actualGenre.name());
        assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && expectedCategories.containsAll(actualGenre.categories().stream().map(CategoryID::from).toList()));
        assertEquals(expectedActive, actualGenre.active());
        assertEquals(actualGenre.createdAt(), createdGenre.getCreatedAt());
        assertTrue(createdGenre.getUpdatedAt().isBefore(actualGenre.updatedAt()));
        assertNotNull(actualGenre.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToNavigateThroughAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());

        givenAGenre("Action", List.of());
        givenAGenre("Sports", List.of());
        givenAGenre("Drama", List.of());

        assertEquals(3, repository.count());

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));

        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSearchByTermsThroughAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());

        givenAGenre("Action", List.of());
        givenAGenre("Sports", List.of());
        givenAGenre("Drama", List.of());

        assertEquals(3, repository.count());

        listGenres(0, 1, "sport", "", "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleListAndSortAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());

        givenAGenre("Action", List.of());
        givenAGenre("Sports", List.of());
        givenAGenre("Drama", List.of());

        assertEquals(3, repository.count());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Action")));
    }

    @Test
    void asCatalogAdminIShouldBeAbleToGetAnGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());
        // given
        final var movies = givenACategory("Movies", "The most watched movies");
        final var series = givenACategory("Series", "The most watched series");
        final var documentaries = givenACategory("Documentaries", "The most watched documentaries");
        final var categories = List.of(movies, series, documentaries);

        final var expectedName = "Action";
        final var expectedCategories = mapTo(categories, CategoryID::getValue);

        // when
        final var actualGenreId = givenAGenre(expectedName, categories);
        assertEquals(1, repository.count());

        final var actualGenre = retrieveAGenre(actualGenreId.getValue());

        // then
        assertNotNull(actualGenre);
        assertEquals(actualGenreId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertTrue(actualGenre.active());
        assertTrue(actualGenre.categories().size() == expectedCategories.size()
            && expectedCategories.containsAll(actualGenre.categories()));
        assertNotNull(actualGenre.createdAt());
        assertNotNull(actualGenre.updatedAt());
        assertNull(actualGenre.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToReceiveATreatedMessageWhenGenreIdWasNotFound() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());
        // given
        final var expectedName = "Action";

        // when
        givenAGenre(expectedName, List.of());
        assertEquals(1, repository.count());

        final var actualGenreId = GenreID.from("123");

        final var request = get("/genres/{id}", actualGenreId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        this.mvc.perform(request).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        // given
        assertEquals(0, repository.count());
        final var movies = givenACategory("Movies", "The most watched movies");

        final var drama = givenAGenre("Action", List.of(movies));
        final var action = givenAGenre("Drama", List.of(movies));
        assertEquals(2, repository.count());

        // when
        deleteAGenre(drama.getValue()).andExpect(status().isNoContent());
        assertEquals(1, repository.count());
        assertFalse(this.repository.existsById(drama.getValue()));
        assertTrue(this.repository.existsById(action.getValue()));
    }

    @Test
    void asACatalogAdminIShouldBeAbleReceiveNoContentWhenDeletingAGenreWithInvalidIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        // given
        assertEquals(0, repository.count());
        final var movies = givenACategory("Movies", "The most watched movies");

        final var drama = givenAGenre("Action", List.of(movies));
        final var action = givenAGenre("Drama", List.of(movies));
        assertEquals(2, repository.count());
        final var actualGenreId = GenreID.from("1234").getValue();

        // when
        deleteAGenre(actualGenreId).andExpect(status().isNoContent());
        assertEquals(2, repository.count());
        assertTrue(this.repository.existsById(drama.getValue()));
        assertTrue(this.repository.existsById(action.getValue()));
    }


}
