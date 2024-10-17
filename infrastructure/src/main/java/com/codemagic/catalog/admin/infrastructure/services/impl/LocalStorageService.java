package com.codemagic.catalog.admin.infrastructure.services.impl;

import com.codemagic.catalog.admin.domain.video.Resource;
import com.codemagic.catalog.admin.infrastructure.services.StorageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public LocalStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public void deleteAll(Set<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public Optional<Resource> get(String name) {
        return Optional.ofNullable(storage.get(name));
    }

    @Override
    public List<String> list(String prefix) {
        return storage.keySet().stream()
                .filter(name -> name.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(String name, Resource resource) {
        storage.remove(name);
        storage.put(name, resource);
    }
}
