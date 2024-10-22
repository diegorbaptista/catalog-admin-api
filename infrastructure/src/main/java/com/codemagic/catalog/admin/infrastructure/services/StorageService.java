package com.codemagic.catalog.admin.infrastructure.services;

import com.codemagic.catalog.admin.domain.resource.Resource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StorageService {

    void deleteAll(final Set<String> names);

    Optional<Resource> get(final String name);

    Set<String> list(final String prefix);

    void store(final String name, final Resource resource);

}
