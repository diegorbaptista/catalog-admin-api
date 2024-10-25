package com.codemagic.catalog.admin.application.media.upload;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoResource;
import com.codemagic.catalog.admin.domain.video.VideoResourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaGateway;

    @Mock
    private VideoGateway videoGateway;

    @Test
    void givenAValidCommand_whenUploadVideo_thenShouldStoreVideoMedia() {
        // given
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoResourceType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(video));

        when(this.mediaGateway.storeAudioVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedType, actualOutput.mediaType());

        verify(this.videoGateway, times(1))
                .findById(eq(expectedId));

        verify(this.mediaGateway, times(1))
                .storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(this.videoGateway, times(1)).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getVideo().orElseThrow())
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty())
        );
   }

    @Test
    void givenAValidCommand_whenUploadTrailer_thenShouldStoreTrailerMedia() {
        // given
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoResourceType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioMedia(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(video));

        when(this.mediaGateway.storeAudioVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedType, actualOutput.mediaType());

        verify(this.videoGateway, times(1))
                .findById(eq(expectedId));

        verify(this.mediaGateway, times(1))
                .storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(this.videoGateway, times(1)).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getTrailer().orElseThrow())
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty())
        );
    }

    @Test
    void givenAValidCommand_whenUploadBanner_thenShouldStoreBannerMedia() {
        // given
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoResourceType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(video));

        when(this.mediaGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedType, actualOutput.mediaType());

        verify(this.videoGateway, times(1))
                .findById(eq(expectedId));

        verify(this.mediaGateway, times(1))
                .storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(this.videoGateway, times(1)).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getBanner().orElseThrow())
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty())
        );
    }

    @Test
    void givenAValidCommand_whenUploadThumbnail_thenShouldStoreThumbnailMedia() {
        // given
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoResourceType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(video));

        when(this.mediaGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedType, actualOutput.mediaType());

        verify(this.videoGateway, times(1))
                .findById(eq(expectedId));

        verify(this.mediaGateway, times(1))
                .storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(this.videoGateway, times(1)).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getThumbnail().orElseThrow())
                        && actualVideo.getThumbnailHalf().isEmpty())
        );
    }

    @Test
    void givenAValidCommand_whenUploadThumbnailHalf_thenShouldStoreThumbnailHalfMedia() {
        // given
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoResourceType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(video));

        when(this.mediaGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(command);

        // then
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedType, actualOutput.mediaType());

        verify(this.videoGateway, times(1))
                .findById(eq(expectedId));

        verify(this.mediaGateway, times(1))
                .storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(this.videoGateway, times(1)).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getThumbnailHalf().orElseThrow()))
        );
    }

    @Test
    void givenAInvalidVideoId_whenUploadMedia_thenShouldReturnNotFound() {
        // given
        final var video = Fixture.Videos.video();
        final var expectedId = video.getId();
        final var expectedType = VideoResourceType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualException = assertThrows(NotFoundException.class,
                () -> this.useCase.execute(command));

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
