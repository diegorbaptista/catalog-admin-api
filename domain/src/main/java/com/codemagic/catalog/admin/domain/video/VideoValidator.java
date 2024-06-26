package com.codemagic.catalog.admin.domain.video;

import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.Validator;

import java.util.Objects;

public class VideoValidator extends Validator {

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 4000;

    private final Video video;
    
    public VideoValidator(final Video video, final ValidationHandler handler) {
        super(handler);
        this.video = Objects.requireNonNull(video);
    }

    private void validateTitle() {
        if (this.video.getTitle() == null) {
            this.handler().append(new Error("'title' should not be null"));
            return;
        }
        if (this.video.getTitle().isBlank()) {
            this.handler().append(new Error("'title' should not be empty"));
            return;
        }

        if (this.video.getTitle().trim().length() > MAX_TITLE_LENGTH) {
            this.handler().append(new Error("'title' length must be between 1 and 255 characters"));
        }
    }

    private void validateDescription() {
        if (this.video.getDescription() == null) {
            this.handler().append(new Error("'description' should not be null"));
            return;
        }
        if (this.video.getDescription().isBlank()) {
            this.handler().append(new Error("'description' should not be empty"));
            return;
        }

        if (this.video.getDescription().trim().length() > MAX_DESCRIPTION_LENGTH) {
            this.handler().append(new Error("'description' length must be between 1 and 4000 characters"));
        }
    }

    private void validateDuration() {
        if (this.video.getDuration() <= 0) {
            this.handler().append(new Error("'duration' must be greater than zero"));
        }
    }

    private void validateRating() {
        if (this.video.getRating() == null) {
            this.handler().append(new Error("'rating' should not be null"));
        }
    }

    private void validateLaunchedYear() {
        if (this.video.getLaunchedAt() == null) {
            this.handler().append(new Error("'launchedAt' should not be null"));
        }
    }

    @Override
    public void validate() {
        validateTitle();
        validateDescription();
        validateLaunchedYear();
        validateRating();
        validateDuration();
    }

}
