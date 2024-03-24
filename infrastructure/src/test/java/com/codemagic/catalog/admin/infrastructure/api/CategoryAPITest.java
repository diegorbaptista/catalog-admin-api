package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.ControllerTest;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryOutput;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codemagic.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.codemagic.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryOutput;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import com.codemagic.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import com.codemagic.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldCreateACategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";

        final var input = new CreateCategoryRequest(expectedName, expectedDescription);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(CategoryID.from("123"))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())

        ));

    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldReturnANotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var input = new CreateCategoryRequest(expectedName, expectedDescription);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())

        ));
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldReturnAHandledDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "The most watched movies";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var input = new CreateCategoryRequest(expectedName, expectedDescription);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())

        ));
    }

    @Test
    void givenAValidCategoryId_whenCallsGetCategory_thenShouldReturnACategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription);
        final var expectedId = category.getId().getValue();

        when(getCategoryByIdUseCase.execute(expectedId))
                .thenReturn(CategoryOutput.from(category));

        final var request = get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name", equalTo(category.getName())))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedActive)))
                .andExpect(jsonPath("$.created_at", equalTo(category.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(category.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(category.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidCategoryId_whenCallsGetCategory_thenShouldReturnNotFound() throws Exception {
        final var expectedId = "1234";
        final var expectedErrorMessage = "Category with ID 1234 was not found";
        final var expectedErrorCount = 1;

        when(getCategoryByIdUseCase.execute(any())).thenThrow(
                NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var request = get("/categories/{id}", expectedId);
        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(getCategoryByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnACategoryId() throws Exception {
        final var expectedId = "1234";
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";
        final var expectedActive = true;

        final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedActive);

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var request = put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())
                        && Objects.equals(expectedActive, command.isActive())

        ));
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateCategory_thenShouldReturnANotification() throws Exception {
        final var expectedId = "1234";
        final String expectedName = null;
        final var expectedActive = true;
        final var expectedDescription = "The most watched movies";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedActive);

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())
                        && Objects.equals(expectedActive, command.isActive())

        ));
    }

    @Test
    void givenAValidCommandWithInvalidCategoryId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() throws Exception {
        final var expectedId = "1234";
        final String expectedName = "Movies";
        final var expectedActive = true;
        final var expectedDescription = "The most watched movies";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 1234 was not found";

        final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedActive);

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var request = put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())
                        && Objects.equals(expectedActive, command.isActive())

        ));
    }

    @Test
    void givenAValidCategoryID_whenCallsDeleteCategory_thenShouldBeOK() throws Exception {
        final var expectedId = "1234";

        doNothing().when(deleteCategoryUseCase).execute(any());

        final var request = delete("/categories/{id}", expectedId);
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAValidQuery_whenListCategories_thenShouldReturnACategoriesList() throws Exception {
        final var movies = Category.newCategory("Movies", "The most watched movies");
        final var series = Category.newCategory("Series", "The most watched series");
        final var categories = List.of(movies, series);

        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemCount = 2;

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(
                        expectedPage,
                        expectedPerPage,
                        categories.size(),
                        categories.stream().map(CategoryListOutput::from).toList()));

        final var request = get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("terms", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedItemCount)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(movies.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(movies.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(movies.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(movies.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(movies.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(movies.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(movies.getDeletedAt())))
                .andExpect(jsonPath("$.items[1].id", equalTo(series.getId().getValue())))
                .andExpect(jsonPath("$.items[1].name", equalTo(series.getName())))
                .andExpect(jsonPath("$.items[1].description", equalTo(series.getDescription())))
                .andExpect(jsonPath("$.items[1].is_active", equalTo(series.isActive())))
                .andExpect(jsonPath("$.items[1].created_at", equalTo(series.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[1].updated_at", equalTo(series.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.items[1].deleted_at", equalTo(series.getDeletedAt())));

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
            Objects.equals(expectedPage, query.page())
                    && Objects.equals(expectedPerPage, query.perPage())
                    && Objects.equals(expectedTerms, query.terms())
                    && Objects.equals(expectedSort, query.sort())
                    && Objects.equals(expectedDirection, query.direction())
        ));

    }

}
