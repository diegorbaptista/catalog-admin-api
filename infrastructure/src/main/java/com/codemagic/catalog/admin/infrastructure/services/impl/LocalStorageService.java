package com.codemagic.catalog.admin.infrastructure.services.impl;

import com.codemagic.catalog.admin.domain.resource.Resource;
import com.codemagic.catalog.admin.infrastructure.services.StorageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LocalStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public void clear() {
        this.storage.clear();
    }

    public Map<String, Resource> storage() {
        return this.storage;
    }

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
    public Set<String> list(String prefix) {
        return storage.keySet().stream()
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toSet());
    }

    @Override
    public void store(String name, Resource resource) {
        storage.remove(name);
        storage.put(name, resource);
    }
}
