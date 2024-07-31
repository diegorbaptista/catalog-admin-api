package com.codemagic.catalog.admin.domain.util;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtil {
    public static <T, R> Set<R> mapTo(final Set<T> items, final Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
