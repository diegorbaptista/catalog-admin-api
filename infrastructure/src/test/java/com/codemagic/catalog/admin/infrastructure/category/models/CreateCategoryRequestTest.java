package com.codemagic.catalog.admin.infrastructure.category.models;

import com.codemagic.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    void testUnmarshallValidParams() throws IOException {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched movies";

        final var json = """
                {
                "name": "%s",
                "description": "%s"
                }
                """.formatted(
                expectedName,
                expectedDescription);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription);
    }

}
