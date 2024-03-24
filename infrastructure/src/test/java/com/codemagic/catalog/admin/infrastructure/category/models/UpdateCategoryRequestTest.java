package com.codemagic.catalog.admin.infrastructure.category.models;

import com.codemagic.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;

    @Test
    void testUnmarshallValidValues() throws IOException {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched movies";
        final var expectedActive = true;

        final var json = """
                {
                "name": "%s",
                "description": "%s",
                "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive);
    }

    @Test
    void testUnmarshallActiveAsNullValue() throws IOException {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched movies";

        final var json = """
                {
                "name": "%s",
                "description": "%s",
                "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                null);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", null);
    }

}
