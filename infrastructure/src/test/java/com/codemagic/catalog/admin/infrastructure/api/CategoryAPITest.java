package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.ControllerTest;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryOutput;
import com.codemagic.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codemagic.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import com.codemagic.catalog.admin.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldCreateACategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched movies";

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription);

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

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription);

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
                .andExpect(jsonPath("$.errors", hasSize(1)))
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

        final var input = new CreateCategoryApiInput(expectedName, expectedDescription);

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
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())

        ));
    }

    @Test
    void givenAValidCategoryId_whenCallsGetCategory_thenShouldReturnACategory () throws Exception {
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


}
