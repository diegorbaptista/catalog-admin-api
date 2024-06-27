package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.video.AudioMediaVideo;
import com.codemagic.catalog.admin.domain.video.MediaStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AudioMediaVideoTest {

    @Test
    void givenAValidParams_whenCallsCreateAudioVideoMedia_thenShouldReturnANewAudioVideoMedia() {
        // given
        final var expectedChecksum = Fixture.AudioMediaVideo.checksum();
        final var expectedName = Fixture.AudioMediaVideo.name();
        final var expectedRawLocation = Fixture.AudioMediaVideo.location();
        final var expectedEncodedLocation = Fixture.AudioMediaVideo.location().concat(".encoded");
        final var expectedMediaStatus = Fixture.AudioMediaVideo.status();

        // when
        final var actualAudioVideo = AudioMediaVideo.with(expectedChecksum, expectedName,
                expectedRawLocation, expectedEncodedLocation, expectedMediaStatus);

        // then
        assertNotNull(actualAudioVideo);
        assertEquals(expectedChecksum, actualAudioVideo.checksum());
        assertEquals(expectedName, actualAudioVideo.name());
        assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
        assertEquals(expectedEncodedLocation, actualAudioVideo.encodedLocation());
        assertEquals(expectedMediaStatus, actualAudioVideo.status());
    }

    @Test
    void givenAValidParamsWithSameChecksumAndRawLocation_whenComparingEquals_thenShouldBeEquals() {
        // given
        final var expectedChecksum = Fixture.AudioMediaVideo.checksum();
        final var expectedRawLocation = Fixture.AudioMediaVideo.location();

        // when
        final var actualAudioVideo1 = AudioMediaVideo.with(
                expectedChecksum,
                Fixture.AudioMediaVideo.name(),
                expectedRawLocation,
                Fixture.AudioMediaVideo.location().concat(".encoded"),
                Fixture.AudioMediaVideo.status());

        final var actualAudioVideo2 = AudioMediaVideo.with(
                expectedChecksum,
                Fixture.AudioMediaVideo.name(),
                expectedRawLocation,
                Fixture.AudioMediaVideo.location().concat(".encoded"),
                Fixture.AudioMediaVideo.status());

        // then
        assertNotNull(actualAudioVideo1);
        assertNotNull(actualAudioVideo2);
        assertEquals(actualAudioVideo1, actualAudioVideo2);
        assertNotSame(actualAudioVideo1, actualAudioVideo2);
    }

    @Test
    void givenAInvalidParams_whenCallsCreateAudioVideoMedia_thenShouldReturnNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                AudioMediaVideo.with(null, "filename.mp4",
                "/videos/filename.mp4", "/videos/encoded/filename.mp4", MediaStatus.PENDING));

        assertThrows(NullPointerException.class, () ->
                AudioMediaVideo.with("78as8d68as7d6as76", null,
                        "/videos/filename.mp4", "/videos/encoded/filename.mp4", MediaStatus.PENDING));

        assertThrows(NullPointerException.class, () ->
                AudioMediaVideo.with("78as8d68as7d6as76", "filename.mp4",
                        null, "/videos/encoded/filename.mp4", MediaStatus.PENDING));

        assertThrows(NullPointerException.class, () ->
                AudioMediaVideo.with("78as8d68as7d6as76", "filename.mp4",
                        "/videos/filename.mp4", null, MediaStatus.PENDING));

        assertThrows(NullPointerException.class, () ->
                AudioMediaVideo.with("78as8d68as7d6as76", "filename.mp4",
                        "/videos/filename.mp4", "/videos/encoded/filename.mp4", null));
    }
}
