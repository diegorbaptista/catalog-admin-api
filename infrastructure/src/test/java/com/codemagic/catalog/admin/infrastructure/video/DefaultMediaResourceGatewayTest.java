package com.codemagic.catalog.admin.infrastructure.video;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.IntegrationTest;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.MediaStatus;
import com.codemagic.catalog.admin.domain.video.VideoID;
import com.codemagic.catalog.admin.domain.video.VideoResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codemagic.catalog.admin.domain.video.VideoResourceType.*;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway gateway;

    @Test
    void testDependencies() {
        assertNotNull(gateway);
        System.out.println(gateway);
    }

    @Test
    void givenAValidAudioVideoMedia_whenCallsStore_thenShouldStoreAnAudioVideo() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VIDEO);
        final var videoResource = VideoResource.with(expectedResource, VIDEO);
        final var expectedRawLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), VIDEO.name());
        // when
        final var actualAudioVideo = this.gateway.storeAudioVideo(expectedVideoId, videoResource);

        // then
        assertNotNull(actualAudioVideo.id());
        assertNotNull(actualAudioVideo.rawLocation());
        assertEquals(MediaStatus.PENDING, actualAudioVideo.status());
        assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
        assertEquals(expectedResource.name(), actualAudioVideo.name());
        assertEquals(expectedResource.checksum(), actualAudioVideo.checksum());
        assertTrue(actualAudioVideo.encodedLocation().isBlank());
    }

    @Test
    void givenAValidImageMedia_whenCallsStore_thenShouldStoreAImage() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(BANNER);
        final var videoResource = VideoResource.with(expectedResource, BANNER);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), BANNER.name());
        // when
        final var actualMedia = this.gateway.storeImage(expectedVideoId, videoResource);

        // then
        assertEquals(expectedLocation, actualMedia.location());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());
    }

    @Test
    void givenAValidMedia_whenCallsGetResource_thenShouldReturnAResource() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedBanner = VideoResource.with(Fixture.Videos.resource(BANNER), BANNER);
        final var expectedTrailer = VideoResource.with(Fixture.Videos.resource(TRAILER), TRAILER);
        this.gateway.storeAudioVideo(expectedVideoId, expectedBanner);
        this.gateway.storeImage(expectedVideoId, expectedTrailer);

        final var otherVideoID = VideoID.unique();
        final var otherVideoMedia = VideoResource.with(Fixture.Videos.resource(VIDEO), VIDEO);
        final var otherVideoThumbnail = VideoResource.with(Fixture.Videos.resource(THUMBNAIL), THUMBNAIL);
        this.gateway.storeAudioVideo(otherVideoID, otherVideoMedia);
        this.gateway.storeImage(otherVideoID, otherVideoThumbnail);

        // when
        assertTrue(this.gateway.getResource(expectedVideoId, BANNER).isPresent());
        assertTrue(this.gateway.getResource(expectedVideoId, TRAILER).isPresent());

        assertTrue(this.gateway.getResource(otherVideoID, VIDEO).isPresent());
        assertTrue(this.gateway.getResource(otherVideoID, THUMBNAIL).isPresent());

        // then
        final var actualBanner = this.gateway.getResource(expectedVideoId, BANNER).orElseThrow();
        final var actualTrailer = this.gateway.getResource(expectedVideoId, TRAILER).orElseThrow();
        final var actualVideo = this.gateway.getResource(otherVideoID, VIDEO).orElseThrow();
        final var actualThumbnail = this.gateway.getResource(otherVideoID, THUMBNAIL).orElseThrow();

        assertEquals(expectedBanner.resource(), actualBanner);
        assertEquals(expectedTrailer.resource(), actualTrailer);
        assertEquals(otherVideoMedia.resource(), actualVideo);
        assertEquals(otherVideoThumbnail.resource(), actualThumbnail);
    }

    @Test
    void givenAnValidMediaList_whenCallsClear_thenShouldClearAll() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedBanner = VideoResource.with(Fixture.Videos.resource(BANNER), BANNER);
        final var expectedTrailer = VideoResource.with(Fixture.Videos.resource(TRAILER), TRAILER);
        this.gateway.storeAudioVideo(expectedVideoId, expectedBanner);
        this.gateway.storeImage(expectedVideoId, expectedTrailer);

        final var deletedVideoID = VideoID.unique();
        final var deletedVideoMedia = VideoResource.with(Fixture.Videos.resource(VIDEO), VIDEO);
        final var deletedVideoThumbnail = VideoResource.with(Fixture.Videos.resource(THUMBNAIL), THUMBNAIL);
        this.gateway.storeAudioVideo(deletedVideoID, deletedVideoMedia);
        this.gateway.storeImage(deletedVideoID, deletedVideoThumbnail);

        assertTrue(this.gateway.getResource(expectedVideoId, BANNER).isPresent());
        assertTrue(this.gateway.getResource(expectedVideoId, TRAILER).isPresent());

        assertTrue(this.gateway.getResource(deletedVideoID, VIDEO).isPresent());
        assertTrue(this.gateway.getResource(deletedVideoID, THUMBNAIL).isPresent());

        // when
        this.gateway.clear(deletedVideoID);

        // then
        assertTrue(this.gateway.getResource(deletedVideoID, VIDEO).isEmpty());
        assertTrue(this.gateway.getResource(deletedVideoID, THUMBNAIL).isEmpty());

        assertTrue(this.gateway.getResource(expectedVideoId, BANNER).isPresent());
        assertTrue(this.gateway.getResource(expectedVideoId, TRAILER).isPresent());

        final var actualBanner = this.gateway.getResource(expectedVideoId, BANNER).orElseThrow();
        final var actualTrailer = this.gateway.getResource(expectedVideoId, TRAILER).orElseThrow();
        assertEquals(expectedBanner.resource(), actualBanner);
        assertEquals(expectedTrailer.resource(), actualTrailer);
    }


}