package com.codemagic.catalog.admin.e2e.castmember;

import com.codemagic.catalog.admin.E2ETest;
import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.e2e.MockDsl;
import com.codemagic.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codemagic.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository repository;

    @Autowired
    private CastMemberGateway gateway;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("adm_catalog_db");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
        // given
        assertTrue(MYSQL_CONTAINER.isRunning());
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        // when
        assertEquals(0, this.repository.count());
        final var expectedId = givenACastMember(expectedName, expectedType);
        final var actualMember = this.repository.findById(expectedId.getValue()).orElseThrow();
        assertEquals(1, this.repository.count());

        // then
        assertNotNull(actualMember);
        assertEquals(expectedId.getValue(), actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateAMemberWithValidValues() throws Exception {
        // given
        assertTrue(MYSQL_CONTAINER.isRunning());
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;

        assertEquals(0, this.repository.count());
        final var expectedId = givenACastMember("Wrong name", CastMemberType.ACTOR);
        final var actualMember = this.repository.findById(expectedId.getValue()).orElseThrow();

        assertEquals(1, this.repository.count());
        assertEquals("Wrong name", actualMember.getName());
        assertEquals(CastMemberType.ACTOR, actualMember.getType());

        // when
        final var body = new UpdateCastMemberRequest(expectedName, expectedType);
        final var response = updateACastMember(expectedId.getValue(), body)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var updatedMember = this.repository.findById(expectedId.getValue()).orElseThrow();

        assertNotNull(updatedMember);
        assertEquals(expectedId.getValue(), updatedMember.getId());
        assertEquals(expectedName, updatedMember.getName());
        assertEquals(expectedType, updatedMember.getType());
        assertEquals(updatedMember.getCreatedAt(), actualMember.getCreatedAt());
        assertTrue(actualMember.getUpdatedAt().isBefore(updatedMember.getUpdatedAt()));
    }

    @Test
    void asACatalogAdminIShouldReceiveATreatedErrorMessageWhenUpdatingAMemberWithInvalidValues() throws Exception {
        // given
        assertTrue(MYSQL_CONTAINER.isRunning());
        final String expectedName = null;
        final CastMemberType expectedType = null;
        final var expectedErrorMessage1 = "'name' should not be null";
        final var expectedErrorMessage2 = "'type' should not be null" ;

        assertEquals(0, this.repository.count());
        final var expectedId = givenACastMember("Wrong name", CastMemberType.ACTOR);
        final var actualMember = this.repository.findById(expectedId.getValue()).orElseThrow();

        assertEquals(1, this.repository.count());
        assertEquals("Wrong name", actualMember.getName());
        assertEquals(CastMemberType.ACTOR, actualMember.getType());

        // when
        final var body = new UpdateCastMemberRequest(expectedName, expectedType);

        updateACastMember(expectedId.getValue(), body)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage1)))
                .andExpect(jsonPath("$.errors[1].message", equalTo(expectedErrorMessage2)));

        final var persistedMember = this.repository.findById(expectedId.getValue()).orElseThrow();

        assertNotNull(persistedMember);
        assertEquals(1, this.repository.count());
        assertEquals("Wrong name", persistedMember.getName());
        assertEquals(CastMemberType.ACTOR, persistedMember.getType());
        assertNotNull(persistedMember.getCreatedAt());
        assertNotNull(persistedMember.getUpdatedAt());
    }



}
