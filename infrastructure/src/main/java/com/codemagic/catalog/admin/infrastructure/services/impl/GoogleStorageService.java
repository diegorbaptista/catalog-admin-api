package com.codemagic.catalog.admin.infrastructure.services.impl;

import com.codemagic.catalog.admin.domain.video.Resource;
import com.codemagic.catalog.admin.infrastructure.services.StorageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

public class GoogleStorageService implements StorageService {

    private final String bucket;
    private final Storage storage;

    public GoogleStorageService(final String bucket, final Storage storage) {
        this.bucket = bucket;
        this.storage = storage;
    }

    @Override
    public void deleteAll(final Set<String> names) {
        final var blobs = names.stream()
                .map(name -> BlobId.of(this.bucket, name))
                .toList();

        this.storage.delete(blobs);
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(this.storage.get(this.bucket, name))
                .map(blob -> Resource.with(blob.getContent(), blob.getContentType(), blob.getName(), null));
    }

    @Override
    public List<String> list(final String prefix) {
        final var page = this.storage.list(this.bucket, Storage.BlobListOption.prefix(prefix));

        return StreamSupport.stream(page.iterateAll().spliterator(), false)
                .map(BlobInfo::getBlobId)
                .map(BlobId::getName)
                .toList();
    }

    @Override
    public void store(String name, Resource resource) {
        this.storage.create(BlobInfo.newBuilder(this.bucket, name)
                .setContentType(resource.contentType())
                .setCrc32cFromHexString("")
                .build(), resource.content());
    }
}
