package com.codemagic.catalog.admin.application;

import com.codemagic.catalog.admin.domain.Identifier;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UseCaseTest {

    protected static <R, T> Set<R> toSet(final Set<T> items, final Function<T, R> mapTo) {
        return items.stream()
                .map(mapTo)
                .collect(Collectors.toSet());
    }

    protected static Set<String> asString(final Set<? extends Identifier> items) {
        return items.stream()
                .map(Identifier::getValue)
                .collect(Collectors.toSet());
    }

}
