package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.ControllerTest;
import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberOutput;
import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCastMemberUseCase createCastMemberUseCase;

    @Test
    void testDependencies() {
        assertNotNull(mvc);
        assertNotNull(mapper);
        assertNotNull(createCastMemberUseCase);
    }

    @Test
    void givenAValidRequest_whenCallsCreateMember_thenShouldReturnANewMember() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var requestBody = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        // when
        final var request = post("/cast-members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/cast-members/".concat(expectedId))));

        verify(createCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));

    }

    @Test
    void givenAnInvalidNullNameRequest_whenCallsCreateMember_thenShouldReturnANewMember() throws Exception {
        // given
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";

        final var requestBody = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = post("/cast-members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));

    }

    @Test
    void givenAnInvalidNullTypeAndNameRequest_whenCallsCreateMember_thenShouldReturnANewMember() throws Exception {
        // given
        final var expectedId = "123";
        final String expectedName = null;
        final CastMemberType expectedType = null;
        final var expectedErrorMessage1 = "'name' should not be null";
        final var expectedErrorMessage2 = "'type' should not be null";

        final var requestBody = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(List.of(new Error(expectedErrorMessage1), new Error(expectedErrorMessage2))));

        // when
        final var request = post("/cast-members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage1)))
                .andExpect(jsonPath("$.errors[1].message", equalTo(expectedErrorMessage2)));

        verify(createCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));

    }

}
