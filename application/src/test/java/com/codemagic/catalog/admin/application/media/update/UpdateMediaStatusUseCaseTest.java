package com.codemagic.catalog.admin.application.media.update;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.video.MediaStatus;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoResourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Test
    void givenAValidCommand_whenUpdateVideoCompleted_thenShouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedType = VideoResourceType.VIDEO;
        final var expectedFolder = "encoded";
        final var expectedFilename = "filename.mp4";
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);
        final var expectedVideo = Fixture.Videos.video().setVideo(expectedMedia);
        final var expectedId = expectedVideo.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(expectedVideo));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var command = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                expectedFolder,
                expectedFilename);

        // when
        useCase.execute(command);

        // then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(gateway, times(1)).findById(eq(expectedId));

        verify(gateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();
        final var actualMedia = actualVideo.getVideo().orElseThrow();

        assertTrue(actualVideo.getTrailer().isEmpty());

        assertEquals(expectedMedia.id(), actualMedia.id());
        assertEquals(expectedMedia.name(), actualMedia.name());
        assertEquals(expectedMedia.checksum(), actualMedia.checksum());
        assertEquals(expectedMedia.rawLocation(), actualMedia.rawLocation());
        assertEquals(expectedStatus, actualMedia.status());
        assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualMedia.encodedLocation());
    }

    @Test
    void givenAValidCommand_whenUpdateTrailerCompleted_thenShouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedType = VideoResourceType.TRAILER;
        final var expectedFolder = "encoded";
        final var expectedFilename = "filename.mp4";
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);
        final var expectedVideo = Fixture.Videos.video().setTrailer(expectedMedia);
        final var expectedId = expectedVideo.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(expectedVideo));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var command = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                expectedFolder,
                expectedFilename);

        // when
        useCase.execute(command);

        // then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(gateway, times(1)).findById(eq(expectedId));

        verify(gateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();
        final var actualMedia = actualVideo.getTrailer().orElseThrow();

        assertTrue(actualVideo.getVideo().isEmpty());

        assertEquals(expectedMedia.id(), actualMedia.id());
        assertEquals(expectedMedia.name(), actualMedia.name());
        assertEquals(expectedMedia.checksum(), actualMedia.checksum());
        assertEquals(expectedMedia.rawLocation(), actualMedia.rawLocation());
        assertEquals(expectedStatus, actualMedia.status());
        assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualMedia.encodedLocation());
    }

    @Test
    void givenAValidCommand_whenUpdateVideoProcessing_thenShouldUpdateStatusOnly() {
        // given
        final var expectedStatus = MediaStatus.PROCESSING;
        final var expectedType = VideoResourceType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);
        final var expectedVideo = Fixture.Videos.video().setVideo(expectedMedia);
        final var expectedId = expectedVideo.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(expectedVideo));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var command = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                null,
                null);

        // when
        useCase.execute(command);

        // then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(gateway, times(1)).findById(eq(expectedId));

        verify(gateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();
        final var actualMedia = actualVideo.getVideo().orElseThrow();

        assertTrue(actualVideo.getTrailer().isEmpty());

        assertEquals(expectedMedia.id(), actualMedia.id());
        assertEquals(expectedMedia.name(), actualMedia.name());
        assertEquals(expectedMedia.checksum(), actualMedia.checksum());
        assertEquals(expectedMedia.rawLocation(), actualMedia.rawLocation());
        assertEquals(expectedStatus, actualMedia.status());
        assertTrue(actualMedia.encodedLocation().isBlank());
    }

    @Test
    void givenAValidCommand_whenUpdateTrailerProcessing_thenShouldUpdateStatusOnly() {
        // given
        final var expectedStatus = MediaStatus.PROCESSING;
        final var expectedType = VideoResourceType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);
        final var expectedVideo = Fixture.Videos.video().setTrailer(expectedMedia);
        final var expectedId = expectedVideo.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(expectedVideo));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var command = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                expectedMedia.id(),
                expectedStatus,
                null,
                null);

        // when
        useCase.execute(command);

        // then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(gateway, times(1)).findById(eq(expectedId));

        verify(gateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();
        final var actualMedia = actualVideo.getTrailer().orElseThrow();

        assertTrue(actualVideo.getVideo().isEmpty());

        assertEquals(expectedMedia.id(), actualMedia.id());
        assertEquals(expectedMedia.name(), actualMedia.name());
        assertEquals(expectedMedia.checksum(), actualMedia.checksum());
        assertEquals(expectedMedia.rawLocation(), actualMedia.rawLocation());
        assertEquals(expectedStatus, actualMedia.status());
        assertTrue(actualMedia.encodedLocation().isBlank());
    }

    @Test
    void givenAValidCommand_whenUpdateWithInvalidMediaId_thenShouldDoNothing() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedType = VideoResourceType.TRAILER;
        final var expectedFolder = "encoded";
        final var expectedFilename = "filename.mp4";
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);
        final var expectedVideo = Fixture.Videos.video().setTrailer(expectedMedia);
        final var expectedId = expectedVideo.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(expectedVideo));

        final var command = UpdateMediaStatusCommand.with(
                expectedId.getValue(),
                "invalid-media-id",
                expectedStatus,
                expectedFolder,
                expectedFilename);

        // when
        useCase.execute(command);

        // then
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, never()).update(any());
    }

}
