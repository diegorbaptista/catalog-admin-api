package com.codemagic.catalog.admin.domain.castmember;

import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private final CastMember member;
    private final static int MIN_NAME_LENGTH = 3;
    private final static int MAX_NAME_LENGTH = 255;

    protected CastMemberValidator(final CastMember member, final ValidationHandler handler) {
        super(handler);
        this.member = member;
    }

    @Override
    public void validate() {
        validateName();
        validateType();
    }

    private void validateType() {
        if (this.member.getType() == null) {
            this.handler().append(new Error("'type' should not be null"));
        }
    }

    private void validateName() {
        final var name = this.member.getName();
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
