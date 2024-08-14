package com.codemagic.catalog.admin.domain.util;

import java.util.UUID;

public final class IdentifierUtil {
    public static String uuid() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .toLowerCase();
    }
}
