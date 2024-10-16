package com.codemagic.catalog.admin.domain.util;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtil {
    public static <T, R> Set<R> mapTo(final Set<T> items, final Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    public static <T> Set<T> nullIfEmpty(final Set<T> items) {
        if (items == null) {
            return null;
        }
        if (items.isEmpty()) {
            return null;
        }
        return items;
    }
}
