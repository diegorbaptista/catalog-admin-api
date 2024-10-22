package com.codemagic.catalog.admin.application.media.get;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @Mock
    private MediaResourceGateway gateway;

    @Test
    void givenAValidCommand_whenGetMedia_thenShouldReturnAResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedMediaType = Fixture.Videos.resourceType();
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);

        when(gateway.getResource(any(), any())).thenReturn(Optional.of(expectedResource));
        final var command = GetMediaCommand.with(expectedId.getValue(), expectedMediaType.name());
        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        verify(gateway, times(1)).getResource(eq(expectedId), eq(expectedMediaType));
        assertEquals(expectedResource.name(), actualOutput.name());
        assertEquals(expectedResource.contentType(), actualOutput.contentType());
        assertEquals(expectedResource.content(), actualOutput.content());
    }

    @Test
    void givenAInvalidVideoId_whenGetMedia_thenShouldReturnAResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedMediaType = Fixture.Videos.resourceType();

        when(gateway.getResource(any(), any())).thenReturn(Optional.empty());
        final var command = GetMediaCommand.with(expectedId.getValue(), expectedMediaType.name());

        // then
        final var actualOutput = assertThrows(NotFoundException.class, () -> this.useCase.execute(command));
        verify(gateway, times(1)).getResource(eq(expectedId), eq(expectedMediaType));
    }

    @Test
    void givenAInvalidMediaType_whenGetMedia_thenShouldReturnAResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedMediaType = "ANY_MEDIA_TYPE";
        final var expectedErrorMessage = "Media type 'ANY_MEDIA_TYPE' does not exists";
        final var command = GetMediaCommand.with(expectedId.getValue(), expectedMediaType);

        // then
        final var actualErrorMessage = assertThrows(NotFoundException.class, () -> this.useCase.execute(command));
        assertEquals(expectedErrorMessage, actualErrorMessage.getMessage());
    }

}
