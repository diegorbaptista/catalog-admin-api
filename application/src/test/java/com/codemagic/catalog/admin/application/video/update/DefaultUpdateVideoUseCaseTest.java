package com.codemagic.catalog.admin.application.video.update;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.InternalErrorException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.resource.Resource;
import com.codemagic.catalog.admin.domain.video.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultUpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Test
    void givenAValidCommand_whenCallUpdateVideo_thenShouldUpdateAVideo() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = toSet(Fixture.Categories.categories(), Category::getId);
        final var expectedGenres = toSet(Fixture.Genres.genres(), Genre::getId);
        final var expectedMembers = toSet(Fixture.CastMembers.members(5), CastMember::getId);
        final var expectedTrailer = Fixture.Videos.resource(VideoResourceType.TRAILER);
        final var expectedVideo = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var expectedBanner = Fixture.Videos.resource(VideoResourceType.BANNER);
        final var expectedThumbnail = Fixture.Videos.resource(VideoResourceType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.resource(VideoResourceType.THUMBNAIL_HALF);

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));

        // then
        assertNotNull(actualVideo);
        assertEquals(actualOutput.id(), actualVideo.getId().getValue());

        verify(videoGateway, times(1)).findById(eq(actualVideo.getId()));

        verify(videoGateway, times(1)).update(argThat(video ->
                Objects.equals(actualVideo.getId(), video.getId())
                        && Objects.equals(expectedTitle, video.getTitle())
                        && Objects.equals(expectedDescription, video.getDescription())
                        && Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, video.getDuration())
                        && Objects.equals(expectedRating, video.getRating().name())
                        && Objects.equals(expectedOpened, video.isOpened())
                        && Objects.equals(expectedPublished, video.isPublished())
                        && Objects.equals(expectedCategories, video.getCategories())
                        && Objects.equals(expectedGenres, video.getGenres())
                        && Objects.equals(expectedMembers, video.getCastMembers())
                        && Objects.equals(expectedTrailer.name(), video.getTrailer().orElseThrow().name())
                        && Objects.equals(expectedVideo.name(), video.getVideo().orElseThrow().name())
                        && Objects.equals(expectedBanner.name(), video.getBanner().orElseThrow().name())
                        && Objects.equals(expectedThumbnail.name(), video.getThumbnail().orElseThrow().name())
                        && Objects.equals(expectedThumbnailHalf.name(), video.getThumbnailHalf().orElseThrow().name())
                        && Objects.equals(actualVideo.getCreatedAt(), video.getCreatedAt())
                        && actualVideo.getUpdatedAt().isBefore(video.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateVideoWithoutCategories_thenShouldReturnAVideoId() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = toSet(Fixture.Genres.genres(), Genre::getId);
        final var expectedMembers = toSet(Fixture.CastMembers.members(5), CastMember::getId);
        final var expectedTrailer = Fixture.Videos.resource(VideoResourceType.TRAILER);
        final var expectedVideo = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var expectedBanner = Fixture.Videos.resource(VideoResourceType.BANNER);
        final var expectedThumbnail = Fixture.Videos.resource(VideoResourceType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.resource(VideoResourceType.THUMBNAIL_HALF);

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));

        // then
        assertNotNull(actualVideo);
        assertEquals(actualOutput.id(), actualVideo.getId().getValue());

        verify(videoGateway, times(1)).findById(eq(actualVideo.getId()));

        verify(videoGateway, times(1)).update(argThat(video ->
                Objects.equals(actualVideo.getId(), video.getId())
                        && Objects.equals(expectedTitle, video.getTitle())
                        && Objects.equals(expectedDescription, video.getDescription())
                        && Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, video.getDuration())
                        && Objects.equals(expectedRating, video.getRating().name())
                        && Objects.equals(expectedOpened, video.isOpened())
                        && Objects.equals(expectedPublished, video.isPublished())
                        && Objects.equals(expectedCategories, video.getCategories())
                        && Objects.equals(expectedGenres, video.getGenres())
                        && Objects.equals(expectedMembers, video.getCastMembers())
                        && Objects.equals(expectedTrailer.name(), video.getTrailer().orElseThrow().name())
                        && Objects.equals(expectedVideo.name(), video.getVideo().orElseThrow().name())
                        && Objects.equals(expectedBanner.name(), video.getBanner().orElseThrow().name())
                        && Objects.equals(expectedThumbnail.name(), video.getThumbnail().orElseThrow().name())
                        && Objects.equals(expectedThumbnailHalf.name(), video.getThumbnailHalf().orElseThrow().name())
                        && Objects.equals(actualVideo.getCreatedAt(), video.getCreatedAt())
                        && actualVideo.getUpdatedAt().isBefore(video.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateVideoWithoutGenres_thenShouldReturnAVideoId() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = toSet(Fixture.Categories.categories(), Category::getId);
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = toSet(Fixture.CastMembers.members(5), CastMember::getId);
        final var expectedTrailer = Fixture.Videos.resource(VideoResourceType.TRAILER);
        final var expectedVideo = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var expectedBanner = Fixture.Videos.resource(VideoResourceType.BANNER);
        final var expectedThumbnail = Fixture.Videos.resource(VideoResourceType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.resource(VideoResourceType.THUMBNAIL_HALF);

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));

        // then
        assertNotNull(actualVideo);
        assertEquals(actualOutput.id(), actualVideo.getId().getValue());

        verify(videoGateway, times(1)).findById(eq(actualVideo.getId()));

        verify(videoGateway, times(1)).update(argThat(video ->
                Objects.equals(actualVideo.getId(), video.getId())
                        && Objects.equals(expectedTitle, video.getTitle())
                        && Objects.equals(expectedDescription, video.getDescription())
                        && Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, video.getDuration())
                        && Objects.equals(expectedRating, video.getRating().name())
                        && Objects.equals(expectedOpened, video.isOpened())
                        && Objects.equals(expectedPublished, video.isPublished())
                        && Objects.equals(expectedCategories, video.getCategories())
                        && Objects.equals(expectedGenres, video.getGenres())
                        && Objects.equals(expectedMembers, video.getCastMembers())
                        && Objects.equals(expectedTrailer.name(), video.getTrailer().orElseThrow().name())
                        && Objects.equals(expectedVideo.name(), video.getVideo().orElseThrow().name())
                        && Objects.equals(expectedBanner.name(), video.getBanner().orElseThrow().name())
                        && Objects.equals(expectedThumbnail.name(), video.getThumbnail().orElseThrow().name())
                        && Objects.equals(expectedThumbnailHalf.name(), video.getThumbnailHalf().orElseThrow().name())
                        && Objects.equals(actualVideo.getCreatedAt(), video.getCreatedAt())
                        && actualVideo.getUpdatedAt().isBefore(video.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallUpdateVideoWithoutCastMembers_thenShouldReturnAVideoId() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = toSet(Fixture.Categories.categories(), Category::getId);
        final var expectedGenres = toSet(Fixture.Genres.genres(), Genre::getId);
        final var expectedMembers = Set.<CastMemberID>of();
        final var expectedTrailer = Fixture.Videos.resource(VideoResourceType.TRAILER);
        final var expectedVideo = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var expectedBanner = Fixture.Videos.resource(VideoResourceType.BANNER);
        final var expectedThumbnail = Fixture.Videos.resource(VideoResourceType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.resource(VideoResourceType.THUMBNAIL_HALF);

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));

        // then
        assertNotNull(actualVideo);
        assertEquals(actualOutput.id(), actualVideo.getId().getValue());

        verify(videoGateway, times(1)).findById(eq(actualVideo.getId()));

        verify(videoGateway, times(1)).update(argThat(video ->
                Objects.equals(actualVideo.getId(), video.getId())
                        && Objects.equals(expectedTitle, video.getTitle())
                        && Objects.equals(expectedDescription, video.getDescription())
                        && Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, video.getDuration())
                        && Objects.equals(expectedRating, video.getRating().name())
                        && Objects.equals(expectedOpened, video.isOpened())
                        && Objects.equals(expectedPublished, video.isPublished())
                        && Objects.equals(expectedCategories, video.getCategories())
                        && Objects.equals(expectedGenres, video.getGenres())
                        && Objects.equals(expectedMembers, video.getCastMembers())
                        && Objects.equals(expectedTrailer.name(), video.getTrailer().orElseThrow().name())
                        && Objects.equals(expectedVideo.name(), video.getVideo().orElseThrow().name())
                        && Objects.equals(expectedBanner.name(), video.getBanner().orElseThrow().name())
                        && Objects.equals(expectedThumbnail.name(), video.getThumbnail().orElseThrow().name())
                        && Objects.equals(expectedThumbnailHalf.name(), video.getThumbnailHalf().orElseThrow().name())
                        && Objects.equals(actualVideo.getCreatedAt(), video.getCreatedAt())
                        && actualVideo.getUpdatedAt().isBefore(video.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateVideoWithoutResources_thenShouldReturnVideoId() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = toSet(Fixture.Categories.categories(), Category::getId);
        final var expectedGenres = toSet(Fixture.Genres.genres(), Genre::getId);
        final var expectedMembers = toSet(Fixture.CastMembers.members(5), CastMember::getId);
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        // when
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));
        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedTrailer,
                expectedVideo,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(command));

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(videoGateway, times(1)).update(argThat(video ->
                Objects.equals(actualVideo.getId(), video.getId())
                        && Objects.equals(expectedTitle, video.getTitle())
                        && Objects.equals(expectedDescription, video.getDescription())
                        && Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, video.getDuration())
                        && Objects.equals(expectedRating, video.getRating().name())
                        && Objects.equals(expectedOpened, video.isOpened())
                        && Objects.equals(expectedPublished, video.isPublished())
                        && Objects.equals(expectedCategories, video.getCategories())
                        && Objects.equals(expectedGenres, video.getGenres())
                        && Objects.equals(expectedMembers, video.getCastMembers())
                        && video.getTrailer().isEmpty()
                        && video.getVideo().isEmpty()
                        && video.getBanner().isEmpty()
                        && video.getThumbnail().isEmpty()
                        && video.getThumbnailHalf().isEmpty()
                        && Objects.equals(actualVideo.getCreatedAt(), video.getCreatedAt())
                        && actualVideo.getUpdatedAt().isBefore(video.getUpdatedAt()))
        );
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateVideoWithNullTitle_thenShouldReturnADomainException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateVideoWithEmptyTitle_thenShouldReturnADomainException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = "  ";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateVideoWithInvalidLength_thenShouldReturnADomainException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.lorem(3000);
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'title' length must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidNullRating_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final String expectedRating = null;
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidRating_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = "AGE_INVALID_1000";
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidNullYear_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchedAt = null;
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidDuration_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = -100.00;
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'duration' must be greater than zero";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidNullDescription_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final String expectedDescription = null;
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'description' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnEmptyDescription_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = "  ";
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'description' should not be empty";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidDescriptionLength_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(5000);
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "'description' length must be between 1 and 4000 characters";
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, never()).update(any());
        verify(categoryGateway, never()).existsByIds(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidCategoryId_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var categoryId = Fixture.Categories.movies().getId();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.of(categoryId);
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(categoryId.getValue());
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(List.of());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(videoGateway, never()).update(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidGenreId_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var genreId = Fixture.Genres.action().getId();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(genreId);
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(genreId.getValue());
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(genreGateway.existsByIds(any())).thenReturn(Collections.emptyList());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, never()).existsByIds(any());
        verify(videoGateway, never()).update(any());
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAnInvalidCastMemberId_whenCallsUpdateVideo_thenShouldReturnANotificationException() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var memberId = Fixture.CastMembers.member().getId();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.title();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(memberId);
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(memberId.getValue());
        final var expectedErrorCount = 1;

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(castMemberGateway.existsByIds(any())).thenReturn(Collections.emptyList());
        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, never()).existsByIds(any());
        verify(videoGateway, never()).update(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateVideoThrowsAnError_thenShouldNotClearResources() {
        // given
        final var actualVideo = Fixture.Videos.video();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.title();
        final var expectedLaunchedAt = Fixture.Videos.launchedAt();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating().name();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedTrailer = null;
        final Resource expectedVideo = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;
        final var expectedErrorMessage = "An error on update video was thrown [id: %s]".formatted(actualVideo.getId().getValue());

        final var command = UpdateVideoCommand.with(
                actualVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedRating,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(actualVideo)));
        doThrow(InternalErrorException.with("An internal server error", new RuntimeException(expectedErrorMessage)))
                .when(videoGateway).update(any());

        // when
        final var actualException = assertThrows(InternalErrorException.class, () -> this.useCase.execute(command));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, never()).existsByIds(any());
        verify(videoGateway, times(1)).update(any());
        verify(genreGateway, never()).existsByIds(any());
        verify(castMemberGateway, never()).existsByIds(any());
        verify(mediaResourceGateway, never()).clear(any());
        verify(mediaResourceGateway, never()).storeImage(any(), any());
        verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any())).thenAnswer(answer -> {
            final var videoResource = answer.getArgument(1, VideoResource.class);
            final var name = videoResource.resource().name();
            final var checksum = videoResource.resource().checksum();
            return ImageMedia.with(checksum, name, "/images/".concat(name));
        });
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer(answer -> {
            final var videoResource = answer.getArgument(1, VideoResource.class);
            final var name = videoResource.resource().name();
            final var checksum = videoResource.resource().checksum();
            return AudioMediaVideo.with(checksum, name,
                    "/videos/".concat(name), "", MediaStatus.PENDING);
        });
    }

}
