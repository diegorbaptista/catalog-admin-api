package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import com.codemagic.catalog.admin.domain.video.AudioMediaVideo;
import com.codemagic.catalog.admin.domain.video.ImageMedia;
import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Video;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    void givenAValidParams_whenCallsNewVideo_thenShouldCreateANewVideo() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        // when
        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoTitle_thenUpdateVideoTitle() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoDescription_thenUpdateVideoDescription() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoLaunchedYear_thenUpdateVideoLaunchedYear() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(2021);
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                Year.of(2011),
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertNotEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoDuration_thenUpdateVideoDuration() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                Year.of(2011),
                80.00,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertNotEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertNotEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoRating_thenUpdateVideoRating() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Rating.L;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                Year.of(2011),
                80.00,
                Rating.AGE_18,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertNotEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertNotEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertNotEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoOpened_thenUpdateVideoOpened() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = true;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                false,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertNotEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateVideoPublished_thenUpdateVideoPublished() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = true;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                false,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertNotEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateCategories_thenUpdateCategories() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                Collections.emptySet(),
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertNotEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateGenres_thenUpdateGenres() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                Collections.emptySet(),
                Collections.emptySet(),
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertNotEquals(expectedCategories, actualVideo.getCategories());
        assertNotEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateCastMembers_thenUpdateCastMembers() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                "Wrong title name",
                "Wrong video description",
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotEquals(expectedTitle, actualVideo.getTitle());
        assertNotEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertNotEquals(expectedCategories, actualVideo.getCategories());
        assertNotEquals(expectedGenres, actualVideo.getGenres());
        assertNotEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video.with(actualVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateBanner_thenUpdateBanner() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedBanner = ImageMedia.with(Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video
                .with(actualVideo)
                .setBanner(expectedBanner);

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isPresent());
        assertEquals(expectedBanner, updatedVideo.getBanner().orElse(null));
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateThumbnail_thenUpdateThumbnail() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedThumbnail = ImageMedia.with(Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video
                .with(actualVideo)
                .setThumbnail(expectedThumbnail);

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isPresent());
        assertEquals(expectedThumbnail, updatedVideo.getThumbnail().orElse(null));
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateThumbnailHalf_thenUpdateThumbnailHalf() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedThumbnailHalf = ImageMedia.with(Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video
                .with(actualVideo)
                .setThumbnailHalf(expectedThumbnailHalf);

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isPresent());
        assertEquals(expectedThumbnailHalf, updatedVideo.getThumbnailHalf().orElse(null));

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateTrailer_thenUpdateTrailer() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedTrailer = AudioMediaVideo.with(Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location(),
                Fixture.AudioMediaVideo.location(),
                Fixture.AudioMediaVideo.status()
        );

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video
                .with(actualVideo)
                .setTrailer(expectedTrailer);

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isEmpty());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());
        assertTrue(updatedVideo.getTrailer().isPresent());
        assertEquals(expectedTrailer, updatedVideo.getTrailer().orElse(null));

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenAValidParams_whenCallsUpdateAudioVideoMedia_thenUpdateVideo() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedVideo = AudioMediaVideo.with(Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location(),
                Fixture.AudioMediaVideo.location(),
                Fixture.AudioMediaVideo.status()
        );

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

        // when
        final var updatedVideo = Video
                .with(actualVideo)
                .setVideo(expectedVideo);

        // then
        assertNotNull(actualVideo);
        assertEquals(updatedVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, updatedVideo.getTitle());
        assertEquals(expectedDescription, updatedVideo.getDescription());
        assertEquals(expectedLaunchedAt, updatedVideo.getLaunchedAt());
        assertEquals(expectedDuration, updatedVideo.getDuration());
        assertEquals(expectedOpened, updatedVideo.isOpened());
        assertEquals(expectedPublished, updatedVideo.isPublished());
        assertEquals(expectedRating, updatedVideo.getRating());
        assertEquals(expectedCategories, updatedVideo.getCategories());
        assertEquals(expectedGenres, updatedVideo.getGenres());
        assertEquals(expectedMembers, updatedVideo.getCastMembers());
        assertNotNull(updatedVideo.getCreatedAt());
        assertNotNull(updatedVideo.getUpdatedAt());
        assertTrue(actualVideo.getUpdatedAt().isBefore(updatedVideo.getUpdatedAt()));
        assertTrue(updatedVideo.getVideo().isPresent());
        assertTrue(updatedVideo.getBanner().isEmpty());
        assertTrue(updatedVideo.getThumbnail().isEmpty());
        assertTrue(updatedVideo.getThumbnailHalf().isEmpty());
        assertTrue(updatedVideo.getTrailer().isEmpty());
        assertEquals(expectedVideo, updatedVideo.getVideo().orElse(null));

        assertDoesNotThrow(() -> updatedVideo.validate(new ThrowsValidationHandler()));
    }

}
