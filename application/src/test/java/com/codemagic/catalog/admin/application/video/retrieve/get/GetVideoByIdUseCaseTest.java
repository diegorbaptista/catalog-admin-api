package com.codemagic.catalog.admin.application.video.retrieve.get;

import com.codemagic.catalog.admin.application.Fixture;
import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.video.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    void givenAValidVideoId_whenCallsGetVideoById_thenShouldReturnAVideo() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = toSet(Fixture.Categories.categories(), Category::getId);
        final var expectedGenres = toSet(Fixture.Genres.genres(), Genre::getId);
        final var expectedMembers = toSet(Fixture.CastMembers.members(5), CastMember::getId);
        final var expectedTrailer = audioVideo(Resource.Type.TRAILER);
        final var expectedVideo = audioVideo(Resource.Type.VIDEO);
        final var expectedBanner = image(Resource.Type.BANNER);
        final var expectedThumbnail = image(Resource.Type.THUMBNAIL);
        final var expectedThumbnailHalf = image(Resource.Type.THUMBNAIL_HALF);

        final var video = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers);

        final var expectedVideoId = video.getId();
        video.setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumbnail)
                .setThumbnailHalf(expectedThumbnailHalf);

        // when
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(video)));

        final var actualVideo = assertDoesNotThrow(() -> this.useCase.execute(expectedVideoId.getValue()));

        // then
        assertNotNull(actualVideo);
        assertEquals(expectedVideoId.getValue(), actualVideo.id());
        assertEquals(expectedTitle, actualVideo.title());
        assertEquals(expectedDescription, actualVideo.description());
        assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        assertEquals(expectedRating, actualVideo.rating());
        assertEquals(expectedDuration, actualVideo.duration());
        assertEquals(expectedOpened, actualVideo.opened());
        assertEquals(expectedPublished, actualVideo.published());
        assertEquals(asString(expectedCategories), actualVideo.categories());
        assertEquals(asString(expectedGenres), actualVideo.genres());
        assertEquals(asString(expectedMembers), actualVideo.members());
        assertEquals(expectedVideo, actualVideo.video());
        assertEquals(expectedTrailer, actualVideo.trailer());
        assertEquals(expectedBanner, actualVideo.banner());
        assertEquals(expectedThumbnail, actualVideo.thumbnail());
        assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
        assertEquals(video.getCreatedAt(), actualVideo.createdAt());
        assertEquals(video.getUpdatedAt(), actualVideo.updatedAt());

        verify(videoGateway, times(1)).findById(eq(expectedVideoId));
    }

    @Test
    void givenAnInvalidVideoId_whenCallsGetVideoById_thenShouldReturnNotFound() {
        // given
        final var videoId = VideoID.unique();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(videoId.getValue());

        // when
        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class,
                () -> this.useCase.execute(videoId.getValue()));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private ImageMedia image(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return ImageMedia.with(
                checksum,
                type.name(),
                "/images/".concat(type.name().toLowerCase()));
    }

    private AudioMediaVideo audioVideo(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return AudioMediaVideo.with(
                checksum,
                type.name(),
                "/videos/".concat(type.name().toLowerCase()),
                checksum.concat(type.name()).toLowerCase(),
                MediaStatus.PENDING);
    }

}
