package com.codemagic.catalog.admin.e2e;

import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.infrastructure.category.models.CategoryResponse;
import com.codemagic.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import com.codemagic.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.codemagic.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.codemagic.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.core.util.Json;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

public interface MockDsl {

    MockMvc mvc();

    default GenreID givenAGenre(final String name, final List<CategoryID> categories) throws Exception {
        final var requestBody = new CreateGenreRequest(name, mapTo(categories, CategoryID::getValue));
        final var actualId = given("/genres", requestBody);
        return GenreID.from(actualId);
    }

    default CategoryID givenACategory(final String categoryName, final String categoryDescription) throws Exception {
        final var requestBody = new CreateCategoryRequest(categoryName, categoryDescription);
        final var actualId = given("/categories", requestBody);
        return CategoryID.from(actualId);
    }

    default ResultActions updateACategory(final String id, final UpdateCategoryRequest requestBody) throws Exception {
        return this.update("/categories", id, requestBody);
    }

    default ResultActions updateAGenre(final String id, final UpdateGenreRequest requestBody) throws Exception {
        return this.update("/genres", id, requestBody);
    }

    default GenreResponse retrieveAGenre(final String id) throws Exception {
        return this.retrieve("/genres", id, GenreResponse.class);
    }

    default CategoryResponse retrieveACategory(final String id) throws Exception {
        return this.retrieve("/categories", id, CategoryResponse.class);
    }

    default ResultActions deleteACategory(final String id) throws Exception {
        return this.delete("/categories", id);
    }

    default ResultActions deleteAGenre(final String id) throws Exception {
        return this.delete("/genres", id);
    }

    default ResultActions listCategories(final int page, final int perPage, final String terms, final String sort, final String direction) throws Exception {
        return list("/categories", page, perPage, terms, sort, direction);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return list("/categories", page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String terms, final String sort, final String direction) throws Exception {
        return list("/genres", page, perPage, terms, sort, direction);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return list("/genres", page, perPage, "", "", "");
    }

    private String given(final String url, final Object body) throws Exception {
        final var request = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(Json.mapper().writeValueAsString(body));

        return Objects.requireNonNull(mvc().perform(request)
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .replace("%s/".formatted(url), "");
    }

    private ResultActions update(final String url, final String id, final Object body) throws Exception {
        final var request = put(url.concat("/{id}"), id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(Json.mapper().writeValueAsString(body));

        return mvc().perform(request).andDo(print());
    }

    private <T> T retrieve(final String url, final String id, final Class<T> response) throws Exception {

        final var request = get(url.concat("/").concat(id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var json = mvc().perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.mapper().readValue(json, response);
    }

    private ResultActions delete(final String url, final String id) throws Exception {
        final var request = MockMvcRequestBuilders
                .delete(url.concat("/{id}"), id)
                .contentType(MediaType.APPLICATION_JSON);

        return mvc().perform(request);
    }

    private ResultActions list(final String url,
                               final int page,
                               final int perPage,
                               final String terms,
                               final String sort,
                               final String direction) throws Exception {
        final var request = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("terms", terms)
                .queryParam("sort", sort)
                .queryParam("direction", direction)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        return mvc().perform(request);
    }

    default <A, D> List<D> mapTo(final List<A> actual, Function<A, D> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

}
