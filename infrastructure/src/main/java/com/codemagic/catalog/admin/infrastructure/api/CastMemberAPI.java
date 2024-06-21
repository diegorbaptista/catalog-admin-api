package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codemagic.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "cast-members")
@Tag(name = "Cast members")
public interface CastMemberAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new cast member")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a cast member by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cast member returned successfully"),
            @ApiResponse(responseCode = "404", description = "Cast member not found"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<CastMemberResponse> get(@PathVariable("id") final String id);

    @PutMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a cast member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cast member updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<?> update(@PathVariable("id") final String id, @RequestBody final UpdateCastMemberRequest input);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List and filter all cast members paginated")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter received"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    Pagination<CastMemberListResponse> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "terms", required = false, defaultValue = "") final String terms,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @DeleteMapping(path = "{id}")
    @Operation(summary = "Delete a cast member by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cast member deleted successfully"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown")
    })
    ResponseEntity<Void> delete(@PathVariable("id") final String id);

}
