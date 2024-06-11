package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.codemagic.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.codemagic.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "genres")
@Tag(name = "Genres")
public interface GenreAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new genre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<?> create(@RequestBody CreateGenreRequest input);

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a genre from ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre returned successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<GenreResponse> get(@PathVariable("id") final String id);

    @PutMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a existent genre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<?> update(@PathVariable("id") final String id, @RequestBody final UpdateGenreRequest input);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List and filter all genres paginated")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter received"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    Pagination<GenreListResponse> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "terms", required = false, defaultValue = "") final String terms,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @DeleteMapping(path = "{id}")
    @Operation(summary = "Delete a genre from ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<Void> delete(@PathVariable("id") final String id);

}
