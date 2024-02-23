package com.codemagic.catalog.admin.infrastructure.util;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtil {

    private SpecificationUtil() {}

    public static <T> Specification<T> like(final String property, final String term) {
        return (root, query, criteria) ->
            criteria.like(criteria.upper(root.get(property)), like(term.toUpperCase()));
    }

    public static String like(final String term) {
        return "%".concat(term).concat("%");
    }

}
