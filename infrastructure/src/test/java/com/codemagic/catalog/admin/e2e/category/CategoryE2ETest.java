package com.codemagic.catalog.admin.e2e.category;

import com.codemagic.catalog.admin.E2ETest;
import com.codemagic.catalog.admin.e2e.MockDsl;
import com.codemagic.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.codemagic.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository repository;

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
    void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidParams() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched movies";

        assertEquals(0, repository.count());

        final var actualCategoryId = givenACategory(expectedName, expectedDescription);
        final var actualCategory = assertDoesNotThrow(() -> repository.findById(actualCategoryId.getValue()).orElseThrow());

        assertEquals(1, repository.count());

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToNavigateThroughAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());

        givenACategory("Movies", "The most watched movies");
        givenACategory("Series", "The most watched series");
        givenACategory("Documentaries", "The most watched documentaries");

        assertEquals(3, repository.count());

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Series")));

        listCategories(3, 1)
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

        givenACategory("Movies", "The most watched movies");
        givenACategory("Series", "The most watched series");
        givenACategory("Documentaries", "The most watched documentaries");

        assertEquals(3, repository.count());

        listCategories(0, 1, "mov", "", "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleListAndSortAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, repository.count());

        givenACategory("Movies", "CCC");
        givenACategory("Series", "AAA");
        givenACategory("Documentaries", "ZZZ");

        assertEquals(3, repository.count());

        listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Movies")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Series")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToRetrieveACategoryByItsID() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        final var expectedName = "Series";
        final var expectedDescription = "The most watched tv series of all time";

        assertEquals(0, repository.count());

        final var actualCategoryId = givenACategory(expectedName, expectedDescription);
        assertEquals(1, repository.count());

        final var actualCategory = retrieveACategory(actualCategoryId.getValue());

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertTrue(actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldReceiveATreatedMessageWhenCategoryIdWasNotFound() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var request = get("/categories/".concat("1234"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

       this.mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Category with ID 1234 was not found")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var actualCategoryId = givenACategory("Mov", "The most...");
        assertEquals(1, repository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched movies";
        final var expectedActive = true;

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedActive);

        updateACategory(actualCategoryId.getValue(), requestBody)
                .andExpect(status().isOk());

        final var actualCategory = retrieveACategory(actualCategoryId.getValue());

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedActive, actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var actualCategoryId = givenACategory("Mov", "The most...");
        assertEquals(1, repository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched movies";
        final var expectedActive = false;

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedActive);
        updateACategory(actualCategoryId.getValue(), requestBody)
                .andExpect(status().isOk());

        final var actualCategory = retrieveACategory(actualCategoryId.getValue());

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedActive, actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNotNull(actualCategory.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        final var expectedName = "Series";
        final var expectedDescription = "The most watched tv series of all time";

        assertEquals(0, repository.count());

        final var actualCategoryId = givenACategory(expectedName, expectedDescription);
        assertEquals(1, repository.count());

        deleteACategory(actualCategoryId.getValue())
                .andExpect(status().isNoContent());

        assertEquals(0, repository.count());
        assertFalse(this.repository.existsById(actualCategoryId.getValue()));
    }
}
