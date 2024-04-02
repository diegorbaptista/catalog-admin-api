package com.codemagic.catalog.admin.domain.genre;

import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.Validator;

import java.util.Objects;

public class GenreValidator extends Validator {
    private final static int MIN_NAME_LENGTH = 3;
    private final static int MAX_NAME_LENGTH = 255;
    private final Genre genre;

    public GenreValidator(final Genre genre, final ValidationHandler handler) {
        super(handler);
        this.genre = Objects.requireNonNull(genre);
    }

    @Override
    public void validate() {
        final var name = this.genre.getName();
        if (name == null) {
            this.handler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.handler().append(new Error("'name' should not be empty"));
            return;
        }
        if ((name.trim().length() < MIN_NAME_LENGTH) || (name.trim().length() > MAX_NAME_LENGTH)) {
            this.handler().append(new Error("'name' length must be between 3 and 255 characters"));
        }

    }
}
