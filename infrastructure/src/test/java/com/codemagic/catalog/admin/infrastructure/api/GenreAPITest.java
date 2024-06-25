package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.ControllerTest;
import com.codemagic.catalog.admin.application.genre.create.CreateGenreOutput;
import com.codemagic.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.codemagic.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.get.GenreOutput;
import com.codemagic.catalog.admin.application.genre.retrieve.get.GetGenreByIDUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.list.GenreListOutput;
import com.codemagic.catalog.admin.application.genre.retrieve.list.ListGenresUseCase;
import com.codemagic.catalog.admin.application.genre.update.UpdateGenreOutput;
import com.codemagic.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.codemagic.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIDUseCase getGenreByIDUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenresUseCase listGenresUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnANewGenre() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Drama";
        final var expectedCategories = List.of("456");

        final var input = new CreateGenreRequest(expectedName, expectedCategories);

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        //when
        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/".concat(expectedId)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        //then
        verify(createGenreUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedCategories, command.categories())

        ));

    }

    @Test
    void givenAInvalidCommand_whenCallsCreateGenre_thenShouldReturnANotificationException() throws Exception {
        // given
        final var expectedErrorMessage = "'name' should not be null";
        final String expectedName = null;
        final var expectedCategories = List.of("456", "789");

        final var input = new CreateGenreRequest(expectedName, expectedCategories);

        when(createGenreUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        //when
        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        //then
        verify(createGenreUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedCategories, command.categories())
        ));

    }

    @Test
    void givenAValidGenreID_whenCallsGetGenreById_thenShouldReturnAGenre() throws Exception {
        // given
        final var expectedName = "Horror";
        final var expectedCategories = List.of("123", "456");

        final var genre = Genre.newGenre(expectedName)
                .addCategories(expectedCategories.stream()
                        .map(CategoryID::from)
                        .toList());
        final var expectedId = genre.getId().getValue();

        when(getGenreByIDUseCase.execute(any())).thenReturn(GenreOutput.from(genre));

        //when
        final var request = get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.is_active", equalTo(true)))
                .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories)))
                .andExpect(jsonPath("$.created_at", equalTo(genre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(genre.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(genre.getDeletedAt())));

        verify(getGenreByIDUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidGenreID_whenCallsGetGenreById_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var expectedId = GenreID.from("123");

        when(getGenreByIDUseCase.execute(any())).thenThrow(NotFoundException.with(Genre.class, expectedId));

        //when
        final var request = get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getGenreByIDUseCase, times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnAUpdatedGenre() throws Exception {
        // given
        final var expectedName = "Horror";
        final var expectedIsActive = false;
        final var expectedCategories = List.of("123", "456");

        final var genre = Genre.newGenre("**Honor").addCategories(
                expectedCategories.stream()
                        .map(CategoryID::from)
                        .toList());
        final var expectedId = genre.getId().getValue();

        final var input = new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories);

        when(updateGenreUseCase.execute(any())).thenReturn(UpdateGenreOutput.from(expectedId));

        // when
        final var request = put("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateGenreUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedId, command.id())
                        && Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedIsActive, command.active())
                        && Objects.equals(expectedCategories, command.categories())
        ));
    }


    @Test
    void givenAnInvalidNullName_whenCallsUpdateGenre_thenShouldReturnABadRequestWithANotificationException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedIsActive = false;
        final var expectedCategories = List.of("123", "456");
        final var expectedErrorMessage = "'name' should not be null";

        final var genre = Genre.newGenre("**Honor").addCategories(
                expectedCategories.stream()
                        .map(CategoryID::from)
                        .toList());
        final var expectedId = genre.getId().getValue();

        final var input = new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories);
        when(updateGenreUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = put("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedId, command.id())
                        && Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedIsActive, command.active())
                        && Objects.equals(expectedCategories, command.categories())
        ));
    }

    @Test
    void givenAnEmptyName_whenCallsUpdateGenre_thenShouldReturnABadRequestWithANotificationException() throws Exception {
        // given
        final var expectedName = "  ";
        final var expectedIsActive = false;
        final var expectedCategories = List.of("123", "456");
        final var expectedErrorMessage = "'name' should not be empty";

        final var genre = Genre.newGenre("**Honor").addCategories(
                expectedCategories.stream()
                        .map(CategoryID::from)
                        .toList());
        final var expectedId = genre.getId().getValue();

        final var input = new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories);
        when(updateGenreUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = put("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedId, command.id())
                        && Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedIsActive, command.active())
                        && Objects.equals(expectedCategories, command.categories())
        ));
    }


    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedName = "Horror";
        final var genre = Genre.newGenre(expectedName);
        final var expectedId = genre.getId().getValue();

        doNothing().when(deleteGenreUseCase).execute(any());

        // when
        final var request = delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteGenreUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedId = GenreID.from("123").getValue();

        doNothing().when(deleteGenreUseCase).execute(any());

        // when
        final var request = delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteGenreUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAValidQuery_whenCallsListGenres_thenShouldReturnPaginatedGenresList() throws Exception {
        // given
        final var seriesID = Category.newCategory("Series", null).getId();
        final var moviesID = Category.newCategory("Movies", null).getId();
        final var documentariesID = Category.newCategory("Documentaries", null).getId();

        final var horror = Genre.newGenre("Horror").addCategories(List.of(seriesID, moviesID, documentariesID));
        final var drama = Genre.newGenre("Drama").addCategories(List.of(seriesID, moviesID));
        final var genres = List.of(horror, drama);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 2;

        when(listGenresUseCase.execute(any())).thenReturn(new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedItemsCount,
                genres.stream().map(GenreListOutput::from).toList()
        ));

        // when
        final var request = get("/genres")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("terms", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection);

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedItemsCount)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(horror.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(horror.getName())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(horror.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(horror.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].categories_id", hasSize(horror.getCategories().size())))
                .andExpect(jsonPath("$.items[0].categories_id[0]", equalTo(seriesID.getValue())))
                .andExpect(jsonPath("$.items[0].categories_id[1]", equalTo(moviesID.getValue())))
                .andExpect(jsonPath("$.items[0].categories_id[2]", equalTo(documentariesID.getValue())))
                .andExpect(jsonPath("$.items[1].id", equalTo(drama.getId().getValue())))
                .andExpect(jsonPath("$.items[1].name", equalTo(drama.getName())))
                .andExpect(jsonPath("$.items[1].is_active", equalTo(drama.isActive())))
                .andExpect(jsonPath("$.items[1].created_at", equalTo(drama.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[1].categories_id", hasSize(drama.getCategories().size())))
                .andExpect(jsonPath("$.items[1].categories_id[0]", equalTo(seriesID.getValue())))
                .andExpect(jsonPath("$.items[1].categories_id[1]", equalTo(moviesID.getValue())));

        verify(listGenresUseCase, times(1)).execute(argThat(searchQuery ->
                Objects.equals(expectedPage, searchQuery.page())
                        && Objects.equals(expectedPerPage, searchQuery.perPage())
                        && Objects.equals(expectedTerms, searchQuery.terms())
                        && Objects.equals(expectedSort, searchQuery.sort())
                        && Objects.equals(expectedDirection, searchQuery.direction())
        ));
    }

}
