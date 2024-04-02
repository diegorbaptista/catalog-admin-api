package com.codemagic.catalog.admin.domain.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantUtil {
    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
