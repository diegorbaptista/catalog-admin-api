package com.codemagic.catalog.admin.infrastructure.api;

import com.codemagic.catalog.admin.ControllerTest;
import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberOutput;
import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.get.CastMemberOutput;
import com.codemagic.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIDUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.list.ListCastMembersOutput;
import com.codemagic.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.codemagic.catalog.admin.application.castmember.update.UpdateCastMemberOutput;
import com.codemagic.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codemagic.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private UpdateCastMemberUseCase updateCastMemberUseCase;

    @MockBean
    private GetCastMemberByIDUseCase getCastMemberByIDUseCase;

    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private ListCastMembersUseCase listCastMembersUseCase;

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
        final var expectedType = Fixture.CastMembers.type();

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
    void givenAnInvalidNullNameRequest_whenCallsCreateMember_thenShouldReturnANotificationException() throws Exception {
        // given
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
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
    void givenAnInvalidNullTypeAndNameRequest_whenCallsCreateMember_thenShouldReturnANotificationException() throws Exception {
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

    @Test
    void givenAValidCommand_whenCallsUpdateCastMember_thenShouldReturnAUpdatedMember() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var requestBody = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenReturn(UpdateCastMemberOutput.from(expectedId));

        // when
        final var request = put("/cast-members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));
    }

    @Test
    void givenAnInvalidMemberId_whenCallsUpdateCastMember_thenShouldReturnANotFoundException() throws Exception {
        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var requestBody = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        // when
        final var request = put("/cast-members/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));
    }

    @Test
    void givenAnInvalidNullName_whenCallsUpdateCastMember_thenShouldReturnANotificationException() throws Exception {
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var requestBody = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = put("/cast-members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));
    }

    @Test
    void givenAnInvalidNullType_whenCallsUpdateCastMember_thenShouldReturnANotificationException() throws Exception {
        final var expectedId = "123";
        final String expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var requestBody = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = put("/cast-members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(command ->
                Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedType, command.type())));
    }

    @Test
    void givenAValidMemberId_whenCallsGetMemberById_thenShouldReturnAMember() throws Exception {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();

        when(getCastMemberByIDUseCase.execute(any()))
                .thenReturn(CastMemberOutput.from(member));

        final var request = get("/cast-members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.type", equalTo(expectedType.name())))
                .andExpect(jsonPath("$.created_at", equalTo(member.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(member.getUpdatedAt().toString())));

        verify(getCastMemberByIDUseCase, times(1)).execute(any());
        verify(getCastMemberByIDUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidMemberId_whenCallsGetMemberById_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedId = CastMemberID.from("1234");
        final var expectedErrorMessage = "CastMember with ID 1234 was not found";

        when(getCastMemberByIDUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = get("/cast-members/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getCastMemberByIDUseCase, times(1)).execute(any());
        verify(getCastMemberByIDUseCase, times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    void givenAValidMemberId_whenCallsDeleteById_thenShouldBeReturnNoContent() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();

        doNothing().when(deleteCastMemberUseCase).execute(any());

        // when
        final var request = delete("/cast-members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isNoContent())
                .andExpect(header().string("Location", nullValue()));

        verify(deleteCastMemberUseCase, times(1)).execute(any());
        verify(deleteCastMemberUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidMemberId_whenCallsDeleteById_thenShouldBeReturnNoContent() throws Exception {
        final var expectedId = CastMemberID.from("123").getValue();

        doNothing().when(deleteCastMemberUseCase).execute(any());

        // when
        final var request = delete("/cast-members/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isNoContent())
                .andExpect(header().string("Location", nullValue()));

        verify(deleteCastMemberUseCase, times(1)).execute(any());
        verify(deleteCastMemberUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAValidQuery_whenCallsListMembers_thenShouldReturnAPaginatedMembersList() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var member1 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var member2 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var members = List.of(member1, member2);

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedItemsCount,
                members.stream().map(ListCastMembersOutput::from).toList()
        );

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(expectedPagination);

        // when
        final var request = get("/cast-members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("terms", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection);

        // when
        final var response = this.mvc.perform(request).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedItemsCount)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(member1.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(member1.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(member1.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(member1.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[1].id", equalTo(member2.getId().getValue())))
                .andExpect(jsonPath("$.items[1].name", equalTo(member2.getName())))
                .andExpect(jsonPath("$.items[1].type", equalTo(member2.getType().name())))
                .andExpect(jsonPath("$.items[1].created_at", equalTo(member2.getCreatedAt().toString())));

        verify(listCastMembersUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedTerms, query.terms())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedDirection, query.direction())
        ));
    }
}
