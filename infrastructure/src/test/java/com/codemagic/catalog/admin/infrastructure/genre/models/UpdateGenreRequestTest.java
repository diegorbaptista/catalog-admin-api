package com.codemagic.catalog.admin.infrastructure.genre.models;

import com.codemagic.catalog.admin.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class UpdateGenreRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    void testUnmarshallValidParamsWithoutCategories() throws IOException {
        final var expectedName = "Drama";
        final var expectedIsActive = true;
        final var json = """
                {
                "name": "%s",
                "is_active": true
                }
                """.formatted(expectedName);

        final var actualJson = this.json.parse(json);
        assertThat(actualJson).hasFieldOrPropertyWithValue("name", expectedName);
        assertThat(actualJson).hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

    @Test
    void testUnmarshallValidParamsWithCategories() throws IOException {
        final var expectedName = "Drama";
        final Boolean expectedIsActive = null;
        final var expectedCategories = List.of("123", "456");

        final var json = """
                {
                "name": "%s",
                "is_active": null,
                "categories_id": [%s]
                }
                """.formatted(expectedName,
                expectedCategories.stream()
                        .map(id -> "\"".concat(id).concat("\""))
                        .collect(Collectors.joining(", ")));

        final var actualJson = this.json.parse(json);
        assertThat(actualJson).hasFieldOrPropertyWithValue("name", expectedName);
        assertThat(actualJson).hasFieldOrPropertyWithValue("active", expectedIsActive);
        assertThat(actualJson).hasFieldOrPropertyWithValue("categories", expectedCategories);
    }


}
