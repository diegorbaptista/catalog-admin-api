package com.codemagic.catalog.admin.infrastructure.services.impl;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.util.IdentifierUtil;
import com.codemagic.catalog.admin.domain.video.VideoResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LocalStorageServiceTest {

    private LocalStorageService service = new LocalStorageService();

    @BeforeEach
    void setup() {
        service.clear();
    }

    @Test
    void givenAValidResource_whenCallsStore_thenShouldStoreAResource() {
        // given
        final var expectedName = "video".concat(IdentifierUtil.uuid());
        final var expectedResource = Fixture.Videos.resource(VideoResourceType.VIDEO);

        // then
        service.store(expectedName, expectedResource);

        // then
        assertEquals(1, service.storage().size());
        assertTrue(service.storage().containsKey(expectedName));
        assertEquals(expectedResource, service.storage().get(expectedName));
    }

    @Test
    void givenAValidName_whenCallsGetResource_thenShouldReturnAResource() {
        // given
        final var expectedName = "video".concat(IdentifierUtil.uuid());
        final var expectedResource = Fixture.Videos.resource(VideoResourceType.VIDEO);

        service.storage().put(expectedName, expectedResource);

        // when
        final var actualResource = this.service.get(expectedName);

        // then
        assertEquals(1, service.storage().size());
        assertTrue(service.storage().containsKey(expectedName));
        assertEquals(expectedResource, actualResource.orElseThrow());
    }

    @Test
    void givenAnInvalidName_whenCallsGetResource_thenShouldReturnEmpty() {
        // given
        final var expectedName = "video".concat(IdentifierUtil.uuid());

        // when
        final var actualResource = this.service.get(expectedName);

        assertEquals(0, service.storage().size());
        assertFalse(service.storage().containsKey(expectedName));
        assertTrue(actualResource.isEmpty());
    }

    @Test
    void givenAValidPrefix_whenCallsList_thenShouldReturnAResourcesNameList() {
        final var expectedVideoNames = List.of(
                "video-".concat(IdentifierUtil.uuid()),
                "video-".concat(IdentifierUtil.uuid()),
                "video-".concat(IdentifierUtil.uuid())
        );

        final var expectedBannerNames = List.of(
                "banner-".concat(IdentifierUtil.uuid()),
                "banner-".concat(IdentifierUtil.uuid())
        );

        expectedVideoNames.forEach(it -> this.service.store(it, Fixture.Videos.resource(VideoResourceType.VIDEO)));
        expectedBannerNames.forEach(it -> this.service.store(it, Fixture.Videos.resource(VideoResourceType.BANNER)));

        // when
        final var actualResult = this.service.list("video");

        // then
        assertEquals(5, this.service.storage().size());
        assertEquals(3, actualResult.size());
        assertTrue(expectedVideoNames.size() == actualResult.size() &&
                actualResult.containsAll(expectedVideoNames));
    }

    @Test
    void givenAnValidNames_whenCallsDeleteAll_thenShouldDeleteAllNames() {
        final var expectedVideoNames = Set.of(
                "video-".concat(IdentifierUtil.uuid()),
                "video-".concat(IdentifierUtil.uuid()),
                "video-".concat(IdentifierUtil.uuid())
        );

        final var expectedBannerNames = Set.of(
                "banner-".concat(IdentifierUtil.uuid()),
                "banner-".concat(IdentifierUtil.uuid())
        );

        expectedVideoNames.forEach(it -> this.service.storage().put(it, Fixture.Videos.resource(VideoResourceType.VIDEO)));
        expectedBannerNames.forEach(it -> this.service.storage().put(it, Fixture.Videos.resource(VideoResourceType.BANNER)));
        assertEquals(5, this.service.storage().size());

        // when
        assertDoesNotThrow(() -> this.service.deleteAll(expectedVideoNames));

        // then
        assertEquals(2, this.service.storage().size());
        assertTrue(expectedBannerNames.size() == this.service.storage().size() &&
                this.service.storage().keySet().containsAll(expectedBannerNames));
    }

}