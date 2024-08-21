package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.DomainException;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoValidator;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VideoValidatorTest {

    @Test
    void givenANullTitle_whenCallsValidate_thenShouldReturnAError() {
        // given
        final String expectedTitle = null;
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenAEmptyTitle_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = " ";
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenAInvalidTitleLength_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title().repeat(500);
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' length must be between 1 and 255 characters";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenANullDescription_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final String expectedDescription = null;
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be null";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenAEmptyDescription_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = "  ";
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenAInvalidDescriptionLength_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(5000);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' length must be between 1 and 4000 characters";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenANullLaunchedYear_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenANullRating_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 120.0;
        final Rating expectedRating = null;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    void givenAInvalidNegativeDuration_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = -120.0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'duration' must be greater than zero";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());

    }

    @Test
    void givenAInvalidZeroDuration_whenCallsValidate_thenShouldReturnAError() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.lorem(500);
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = 0;
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'duration' must be greater than zero";

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
        // when
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, validator::validate);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

}
