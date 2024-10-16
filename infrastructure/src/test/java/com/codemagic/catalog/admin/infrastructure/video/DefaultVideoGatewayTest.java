package com.codemagic.catalog.admin.infrastructure.video;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.video.*;
import com.codemagic.catalog.admin.infrastructure.video.persistence.VideoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember ford;
    private CastMember portman;

    private Category movies;
    private Category series;

    private Genre drama;
    private Genre action;

    @BeforeEach
    void setUp() {
        ford = this.castMemberGateway.create(CastMember.newMember("Harrison Ford", CastMemberType.ACTOR));
        portman = this.castMemberGateway.create(CastMember.newMember("Natalie Portman", CastMemberType.ACTOR));

        movies = this.categoryGateway.create(Fixture.Categories.movies());
        series = this.categoryGateway.create(Fixture.Categories.series());

        drama = this.genreGateway.create(Fixture.Genres.drama());
        action = this.genreGateway.create(Fixture.Genres.action());
    }

    @Test
    void testDependencies() {
        assertNotNull(videoGateway);
        assertNotNull(castMemberGateway);
        assertNotNull(genreGateway);
        assertNotNull(categoryGateway);
        assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    void givenAValidVideo_whenCallsCreateVideo_thenShouldPersistAVideo() {
        // given
        assertEquals(0, videoRepository.count());

        final var movies = categoryGateway.create(Fixture.Categories.movies());
        final var drama = genreGateway.create(Fixture.Genres.drama());
        final var member = castMemberGateway.create(Fixture.CastMembers.member());

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.of(movies.getId());
        final var expectedGenres = Set.of(drama.getId());
        final var expectedMembers = Set.of(member.getId());

        final var expectedTrailer = AudioMediaVideo.with(
                Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location());

        final var expectedVideo = AudioMediaVideo.with(
                Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location());

        final var expectedBanner = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var expectedThumbnail = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var expectedThumbnailHalf = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());


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
                        expectedMembers
                ).setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumbnail)
                .setThumbnailHalf(expectedThumbnailHalf);

        // when
        final var actualVideo = this.videoGateway.create(Video.with(video));

        // then
        assertEquals(1, videoRepository.count());
        assertEquals(video.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedTrailer, actualVideo.getTrailer().orElseThrow());
        assertEquals(expectedVideo, actualVideo.getVideo().orElseThrow());
        assertEquals(expectedBanner, actualVideo.getBanner().orElseThrow());
        assertEquals(expectedThumbnail, actualVideo.getThumbnail().orElseThrow());
        assertEquals(expectedThumbnailHalf, actualVideo.getThumbnailHalf().orElseThrow());
        assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        assertEquals(video.getUpdatedAt(), actualVideo.getUpdatedAt());

        final var persistedVideo = this.videoRepository.findById(actualVideo.getId().getValue())
                .orElseThrow();

        assertEquals(actualVideo.getId().getValue(), persistedVideo.getId());
        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getLaunchedAt()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getMembersID());
        assertEquals(expectedTrailer, persistedVideo.getTrailer().toDomain());
        assertEquals(expectedVideo, persistedVideo.getVideo().toDomain());
        assertEquals(expectedBanner, persistedVideo.getBanner().toDomain());
        assertEquals(expectedThumbnail, persistedVideo.getThumbnail().toDomain());
        assertEquals(expectedThumbnailHalf, persistedVideo.getThumbnailHalf().toDomain());
        assertEquals(actualVideo.getCreatedAt(), persistedVideo.getCreatedAt());
        assertEquals(actualVideo.getUpdatedAt(), persistedVideo.getUpdatedAt());
    }

    @Test
    @Transactional
    void givenAValidVideoWithoutResources_whenCallsCreateVideo_thenShouldPersistAVideo() {
        // given
        assertEquals(0, videoRepository.count());

        final var movies = categoryGateway.create(Fixture.Categories.movies());
        final var drama = genreGateway.create(Fixture.Genres.drama());
        final var member = castMemberGateway.create(Fixture.CastMembers.member());

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.of(movies.getId());
        final var expectedGenres = Set.of(drama.getId());
        final var expectedMembers = Set.of(member.getId());

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
                expectedMembers
        );

        // when
        final var actualVideo = this.videoGateway.create(Video.with(video));

        // then
        assertEquals(1, videoRepository.count());
        assertEquals(video.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        assertEquals(video.getUpdatedAt(), actualVideo.getUpdatedAt());

        final var persistedVideo = this.videoRepository.findById(actualVideo.getId().getValue())
                .orElseThrow();

        assertEquals(actualVideo.getId().getValue(), persistedVideo.getId());
        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getLaunchedAt()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getMembersID());
        assertNull(persistedVideo.getTrailer());
        assertNull(persistedVideo.getVideo());
        assertNull(persistedVideo.getBanner());
        assertNull(persistedVideo.getThumbnail());
        assertNull(persistedVideo.getThumbnailHalf());
        assertEquals(actualVideo.getCreatedAt(), persistedVideo.getCreatedAt());
        assertEquals(actualVideo.getUpdatedAt(), persistedVideo.getUpdatedAt());
    }

    @Test
    @Transactional
    void givenAValidVideoWithoutRelations_whenCallsCreateVideo_thenShouldPersistAVideo() {
        // given
        assertEquals(0, videoRepository.count());

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

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
                expectedMembers
        );

        // when
        final var actualVideo = this.videoGateway.create(Video.with(video));

        // then
        assertEquals(1, videoRepository.count());
        assertEquals(video.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        assertEquals(video.getUpdatedAt(), actualVideo.getUpdatedAt());

        final var persistedVideo = this.videoRepository.findById(actualVideo.getId().getValue())
                .orElseThrow();

        assertEquals(actualVideo.getId().getValue(), persistedVideo.getId());
        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getLaunchedAt()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getMembersID());
        assertNull(persistedVideo.getTrailer());
        assertNull(persistedVideo.getVideo());
        assertNull(persistedVideo.getBanner());
        assertNull(persistedVideo.getThumbnail());
        assertNull(persistedVideo.getThumbnailHalf());
        assertEquals(actualVideo.getCreatedAt(), persistedVideo.getCreatedAt());
        assertEquals(actualVideo.getUpdatedAt(), persistedVideo.getUpdatedAt());
    }

    @Test
    void givenAValidVideoWithoutRelations_whenUpdateAVideo_thenShouldReturnUpdateAVideo() {
        //given
        assertEquals(0, videoRepository.count());

        final var video = Video.with(this.videoGateway.create(Video.newVideo(
                "Fake video name",
                "Fake video description",
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        )));

        assertEquals(1, videoRepository.count());

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.of(movies.getId());
        final var expectedGenres = Set.of(drama.getId());
        final var expectedMembers = Set.of(ford.getId());

        final var expectedTrailer = AudioMediaVideo.with(
                Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location());

        final var expectedVideo = AudioMediaVideo.with(
                Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location());

        final var expectedBanner = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var expectedThumbnail = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var expectedThumbnailHalf = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        // when
        final var actualVideo = this.videoGateway.update(Video.with(video).update(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers)
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumbnail)
                .setThumbnailHalf(expectedThumbnailHalf));

        // then
        assertEquals(video.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedTrailer, actualVideo.getTrailer().orElseThrow());
        assertEquals(expectedVideo, actualVideo.getVideo().orElseThrow());
        assertEquals(expectedBanner, actualVideo.getBanner().orElseThrow());
        assertEquals(expectedThumbnail, actualVideo.getThumbnail().orElseThrow());
        assertEquals(expectedThumbnailHalf, actualVideo.getThumbnailHalf().orElseThrow());
        assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        final var persistedVideo = this.videoRepository.findById(actualVideo.getId().getValue())
                .orElseThrow();

        assertEquals(actualVideo.getId().getValue(), persistedVideo.getId());
        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchedAt, Year.of(persistedVideo.getLaunchedAt()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getMembersID());
        assertEquals(expectedTrailer, persistedVideo.getTrailer().toDomain());
        assertEquals(expectedVideo, persistedVideo.getVideo().toDomain());
        assertEquals(expectedBanner, persistedVideo.getBanner().toDomain());
        assertEquals(expectedThumbnail, persistedVideo.getThumbnail().toDomain());
        assertEquals(expectedThumbnailHalf, persistedVideo.getThumbnailHalf().toDomain());
        assertEquals(actualVideo.getCreatedAt(), persistedVideo.getCreatedAt());
        assertTrue(video.getUpdatedAt().isBefore(persistedVideo.getUpdatedAt()));
    }

    @Test
    @Transactional
    void givenAValidVideoId_whenCallsDeleteVideoById_thenShouldDeleteAVideo() {
        // given
        assertEquals(0, videoRepository.count());

        final var video = Video.with(this.videoGateway.create(Video.newVideo(
                "Fake video name",
                "Fake video description",
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        )));

        assertEquals(1, videoRepository.count());
        final var videoId = video.getId();

        // when
        this.videoGateway.deleteById(videoId);

        // then
        assertEquals(0, videoRepository.count());
    }

    @Test
    @Transactional
    void givenAValidVideoId_whenCallsGetVideoById_thenShouldReturnAVideo() {
        // given
        assertEquals(0, videoRepository.count());

        final var movies = categoryGateway.create(Fixture.Categories.movies());
        final var drama = genreGateway.create(Fixture.Genres.drama());
        final var member = castMemberGateway.create(Fixture.CastMembers.member());

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.Videos.launchedAt());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.Videos.opened();
        final var expectedPublished = Fixture.Videos.published();
        final var expectedCategories = Set.of(movies.getId());
        final var expectedGenres = Set.of(drama.getId());
        final var expectedMembers = Set.of(member.getId());

        final var expectedTrailer = AudioMediaVideo.with(
                Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location());

        final var expectedVideo = AudioMediaVideo.with(
                Fixture.AudioMediaVideo.checksum(),
                Fixture.AudioMediaVideo.name(),
                Fixture.AudioMediaVideo.location());

        final var expectedBanner = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var expectedThumbnail = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var expectedThumbnailHalf = ImageMedia.with(
                Fixture.ImageMedia.checksum(),
                Fixture.ImageMedia.name(),
                Fixture.ImageMedia.location());

        final var video = this.videoGateway.create(
                Video.newVideo(
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
                        ).setVideo(expectedVideo)
                        .setTrailer(expectedTrailer)
                        .setBanner(expectedBanner)
                        .setThumbnail(expectedThumbnail)
                        .setThumbnailHalf(expectedThumbnailHalf));

        // when
        final var actualVideo = assertDoesNotThrow(
                () -> this.videoGateway.findById(video.getId()).orElseThrow());

        // then
        assertEquals(1, videoRepository.count());
        assertEquals(video.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(expectedPublished, actualVideo.isPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedTrailer, actualVideo.getTrailer().orElseThrow());
        assertEquals(expectedVideo, actualVideo.getVideo().orElseThrow());
        assertEquals(expectedBanner, actualVideo.getBanner().orElseThrow());
        assertEquals(expectedThumbnail, actualVideo.getThumbnail().orElseThrow());
        assertEquals(expectedThumbnailHalf, actualVideo.getThumbnailHalf().orElseThrow());
        assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        assertEquals(video.getUpdatedAt(), actualVideo.getUpdatedAt());
    }

    @Test
    void givenAInvalidVideoId_whenCallsGetVideoById_thenShouldReturnEmpty() {

        // given
        assertEquals(0, videoRepository.count());
        final var video = this.videoGateway.create(Fixture.Videos.video());

        // when
        final var actualVideo = assertDoesNotThrow(
                () -> this.videoGateway.findById(VideoID.from("1234")));

        // then
        assertTrue(actualVideo.isEmpty());
    }

    /* [x] 1. Test with ordination by asc or desc and multiple params
     * [x] 2. Test with pagination
     * [x] 3. Test with empty videos datasource
     * [x] 4. Test with search by term
     * [x] 5. Test with search by cast member
     * [x] 6. Test with search by category
     * [x] 7. Test with search by genre
     * [x] 8. Test with search by all terms
     * [x] 9. Test with searching videos without relationships
     * */
    @Test
    void givenVideos_whenCallsFindAll_thenShouldReturnAll() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 6;

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        final var actualPage = this.videoGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    void givenEmptyVideos_whenCallsFindAll_thenShouldReturnAEmptyPage() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        final var actualPage = this.videoGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    void givenAValidCategory_whenCallsFindAll_thenShouldReturnFilteredList() {
        // given
        mockVideos();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(movies.getId()),
                Set.of()
        );

        final var actualPage = this.videoGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("Indiana Jones and the Last Crusade", actualPage.items().get(0).title());
        assertEquals("Star Wars: Episode II – Attack of the Clones", actualPage.items().get(1).title());
        assertEquals("V for Vendetta", actualPage.items().get(2).title());
    }

    @Test
    void givenAValidGenre_whenCallsFindAll_thenShouldReturnFilteredList() {
        // given
        mockVideos();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of(drama.getId())
        );

        final var actualPage = this.videoGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("1923", actualPage.items().get(0).title());
        assertEquals("Breaking Bad", actualPage.items().get(1).title());
        assertEquals("V for Vendetta", actualPage.items().get(2).title());
    }

    @Test
    void givenAValidCastMember_whenCallsFindAll_thenShouldReturnFilteredList() {
        // given
        mockVideos();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(ford.getId()),
                Set.of(),
                Set.of()
        );

        final var actualPage = this.videoGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("1923", actualPage.items().get(0).title());
        assertEquals("Indiana Jones and the Last Crusade", actualPage.items().get(1).title());
    }

    @Test
    void givenAFullParameters_whenCallsFindAll_thenShouldReturnFilteredList() {
        // given
        mockVideos();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "92";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(ford.getId()),
                Set.of(series.getId()),
                Set.of(drama.getId())
        );

        final var actualPage = this.videoGateway.findAll(query);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("1923", actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "92,0,10,1,1,1923",
            "Attack,0,10,1,1,Star Wars: Episode II – Attack of the Clones",
            "jones,0,10,1,1,Indiana Jones and the Last Crusade",
            "dune,0,10,1,1,Dune: Part Two"
    })
    void givenAValidTerms_whenCallFindAll_thenShouldReturnPaginatedAndFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet());
        // when
        final var actualPage = this.videoGateway.findAll(query);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,6,6,1923",
            "title,desc,0,10,6,6,V for Vendetta",
            "createdAt,asc,0,10,6,6,Breaking Bad",
            "createdAt,desc,0,10,6,6,Dune: Part Two",
    })
    void givenAValidSortAndDirection_whenCallFindAll_thenShouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();
        final var expectedTerms = "";
        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet());
        // when
        final var actualPage = this.videoGateway.findAll(query);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,6,1923;Breaking Bad",
            "1,2,2,6,Dune: Part Two;Indiana Jones and the Last Crusade",
            "2,2,2,6,Star Wars: Episode II – Attack of the Clones;V for Vendetta"
    })
    void givenAValidPaging_whenCallFindAll_thenShouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideos
    ) {
        // given
        mockVideos();
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var query = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet());
        // when
        final var actualPage = this.videoGateway.findAll(query);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        var index = 0;
        for(var expectedVideo : expectedVideos.split(";")) {
            assertEquals(expectedVideo, actualPage.items().get(index).title());
            index++;
        }
    }

    @Transactional
    void mockVideos() {
        this.videoGateway.create(Video.newVideo(
                "Breaking Bad",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Set.of(series.getId()),
                Set.of(drama.getId()),
                Collections.emptySet()
        ));

        this.videoGateway.create(Video.newVideo(
                "V for Vendetta",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Set.of(movies.getId()),
                Set.of(drama.getId()),
                Set.of(portman.getId())
        ));

        this.videoGateway.create(Video.newVideo(
                "1923",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Set.of(series.getId()),
                Set.of(drama.getId()),
                Set.of(ford.getId())
        ));

        this.videoGateway.create(Video.newVideo(
                "Star Wars: Episode II – Attack of the Clones",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Set.of(movies.getId()),
                Set.of(action.getId()),
                Set.of(portman.getId())
        ));

        this.videoGateway.create(Video.newVideo(
                "Indiana Jones and the Last Crusade",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Set.of(movies.getId()),
                Set.of(action.getId()),
                Set.of(ford.getId())
        ));

        this.videoGateway.create(Video.newVideo(
                "Dune: Part Two",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.launchedAt()),
                Fixture.Videos.duration(),
                Fixture.Videos.rating(),
                Fixture.Videos.opened(),
                Fixture.Videos.published(),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        ));
    }

}