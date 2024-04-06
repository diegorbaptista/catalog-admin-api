package com.codemagic.catalog.admin.application.genre.create;

import java.util.List;

public record CreateGenreCommand(String name, List<String> categories) {
    public static CreateGenreCommand with(final String name, final List<String> categories) {
        return new CreateGenreCommand(name, categories);
    }
}
