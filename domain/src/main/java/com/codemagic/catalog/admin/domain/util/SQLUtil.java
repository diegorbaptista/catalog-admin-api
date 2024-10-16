package com.codemagic.catalog.admin.domain.util;

public class SQLUtil {
    private SQLUtil() {}

    private static String upper(final String value) {
        if (value == null) {
            return "";
        }
        return value.toUpperCase();
    }

    public static String like(final String value) {
        if (value == null) {
            return null;
        }
        if (value.isBlank()) {
            return null;
        }
        return "%".concat(upper(value)).concat("%");
    }

}
