package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.video.ImageMedia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageMediaTest {

    @Test
    void givenAValidParams_whenCreateAImageMedia_thenShouldReturnANewImageMedia() {
        // given
        final var expectedChecksum = Fixture.ImageMedia.checksum();
        final var expectedName = Fixture.ImageMedia.name();
        final var expectedLocation = Fixture.ImageMedia.location();

        // when
        final var actualImageMedia = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // then
        assertNotNull(actualImageMedia);
        assertEquals(expectedChecksum, actualImageMedia.checksum());
        assertEquals(expectedName, actualImageMedia.name());
        assertEquals(expectedLocation, actualImageMedia.location());
    }

    @Test
    void givenAValidParamsWithSameChecksumAndLocation_whenCompareEquals_thenShouldBeEquals() {
        // given
        final var expectedChecksum = Fixture.ImageMedia.checksum();
        final var expectedLocation = Fixture.ImageMedia.location();

        // when
        final var actualImageMedia1 = ImageMedia.with(expectedChecksum, Fixture.ImageMedia.name(), expectedLocation);
        final var actualImageMedia2 = ImageMedia.with(expectedChecksum, Fixture.ImageMedia.name(), expectedLocation);

        // then
        assertNotNull(actualImageMedia1);
        assertNotNull(actualImageMedia2);
        assertEquals(actualImageMedia1, actualImageMedia2);
        assertNotSame(actualImageMedia1, actualImageMedia2);
    }

    @Test
    void givenANullParams_whenCallsCreateNewImage_thenShouldReturnNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            ImageMedia.with(null, "filename.png", "/images/filename.png"));

        assertThrows(NullPointerException.class, () ->
                ImageMedia.with("a09sd80as87df0as", null, "/images/filename.png"));

        assertThrows(NullPointerException.class, () ->
                ImageMedia.with("a09sd80as87df0as", "filename.png", null));
    }

}