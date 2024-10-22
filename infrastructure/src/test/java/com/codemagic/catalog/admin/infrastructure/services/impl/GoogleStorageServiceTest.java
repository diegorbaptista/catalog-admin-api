package com.codemagic.catalog.admin.infrastructure.services.impl;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.domain.resource.Resource;
import com.codemagic.catalog.admin.domain.util.IdentifierUtil;
import com.codemagic.catalog.admin.domain.video.VideoResourceType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GoogleStorageServiceTest {

    private GoogleStorageService service;
    private Storage storage;
    private String bucket = "test_bucket";

    @BeforeEach
    void setup() {
        this.storage = mock(Storage.class);
        this.service = new GoogleStorageService(this.bucket, this.storage);
    }

    @Test
    void givenAValidResource_whenCallsStore_thenShouldStoreAResource() {
        // given
        final var expectedName = IdentifierUtil.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(this.storage).create(any(BlobInfo.class), any());

        // when
        this.service.store(expectedName, expectedResource);

        // then
        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(this.storage, times(1))
                .create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        assertEquals(expectedName, actualBlob.getName());
        assertEquals(expectedName, actualBlob.getBlobId().getName());
        assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    void givenAValidName_whenCallsGetResource_thenShouldReturnAResource() {
        // given
        final var expectedName = IdentifierUtil.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(this.storage).get(anyString(), anyString());

        // when
        final var actualResource = this.service.get(expectedName);

        //then
        verify(this.storage, times(1))
                .get(eq(this.bucket), eq(expectedName));

        assertEquals(expectedResource, actualResource.orElseThrow());
    }

    @Test
    void givenAnInvalidName_whenCallsGetResource_thenShouldReturnEmpty() {
        // given
        final var expectedName = IdentifierUtil.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoResourceType.VIDEO);
        doReturn(null).when(this.storage).get(anyString(), anyString());

        // when
        final var actualResource = this.service.get(expectedName);

        //then
        verify(this.storage, times(1))
                .get(eq(this.bucket), eq(expectedName));

        assertTrue(actualResource.isEmpty());
    }

    @Test
    void givenAValidPrefix_whenCallsList_thenShouldReturnAResourcesNameList() {
        final var expectedPrefix = "media-";
        final var expectedVideoName = expectedPrefix + IdentifierUtil.uuid();
        final var expectedVideoResource = Fixture.Videos.resource(VideoResourceType.VIDEO);
        final var blobVideo = mockBlob(expectedVideoName, expectedVideoResource);

        final var expectedBannerName = expectedPrefix + IdentifierUtil.uuid();
        final var expectedBannerResource = Fixture.Videos.resource(VideoResourceType.BANNER);
        final var blobBanner = mockBlob(expectedBannerName, expectedBannerResource);

        final var expectedResourcesNames = List.of(expectedBannerName, expectedVideoName);

        final var page = mock(Page.class);
        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();
        doReturn(page).when(this.storage).list(anyString(), any());

        // when
        final var actualResource = this.service.list(expectedPrefix);

        // then
        verify(this.storage, times(1))
                .list(eq(this.bucket), eq(Storage.BlobListOption.prefix(expectedPrefix)));

        assertTrue(expectedResourcesNames.size() == actualResource.size() &&
                actualResource.containsAll(expectedResourcesNames));
    }

    @Test
    void givenAnValidNames_whenCallsDeleteAll_thenShouldDeleteAllNames() {
        final var expectedPrefix = "media-";
        final var expectedVideoName = expectedPrefix + IdentifierUtil.uuid();
        final var expectedBannerName = expectedPrefix + IdentifierUtil.uuid();
        final var expectedResourcesNames = Set.of(expectedBannerName, expectedVideoName);

        // when
        this.service.deleteAll(expectedResourcesNames);

        // then
        final var captor = ArgumentCaptor.forClass(List.class);

        verify(this.storage, times(1))
                .delete(captor.capture());

        final var actualResources = ((List<BlobId>) captor.getValue())
                .stream()
                .map(BlobId::getName)
                .toList();

        assertTrue(expectedResourcesNames.size() == actualResources.size() &&
                actualResources.containsAll(expectedResourcesNames));
    }

    private Blob mockBlob(final String name, final Resource resource) {
        final var blob = mock(Blob.class);
        when(blob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob.getContent()).thenReturn(resource.content());
        when(blob.getContentType()).thenReturn(resource.contentType());
        when(blob.getName()).thenReturn(resource.name());
        return blob;
    }
}