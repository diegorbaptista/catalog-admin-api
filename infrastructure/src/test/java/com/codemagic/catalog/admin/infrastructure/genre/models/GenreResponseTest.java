package com.codemagic.catalog.admin.infrastructure.genre.models;

import com.codemagic.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@JacksonTest
public class GenreResponseTest {

    @Autowired
    private JacksonTester<GenreResponse> json;

    @Test
    void testMarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreResponse(
                expectedId,
                expectedName,
                expectedActive,
                expectedCategories,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.is_active", expectedActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt)
                .hasJsonPathValue("$.categories_id[0]", expectedCategories.get(0))
                .hasJsonPathValue("$.categories_id[1]", expectedCategories.get(1));
    }

    @Test
    void testUnmarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.of("123", "456");
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                    "id": "%s",
                    "name": "%s",
                    "is_active": %s,
                    "categories_id": [%s],
                    "created_at": "%s",
                    "updated_at": "%s",
                    "deleted_at": "%s"
                }
                """.formatted(
                expectedId,
                expectedName,
                expectedActive,
                expectedCategories.stream()
                        .map(s -> "\"".concat(s).concat("\""))
                        .collect(Collectors.joining(",")),
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt);

            final var actualJson = this.json.parse(json);

            Assertions.assertThat(actualJson)
                    .hasFieldOrPropertyWithValue("id", expectedId)
                    .hasFieldOrPropertyWithValue("name", expectedName)
                    .hasFieldOrPropertyWithValue("active", expectedActive)
                    .hasFieldOrPropertyWithValue("categories", expectedCategories)
                    .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                    .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                    .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }

}
