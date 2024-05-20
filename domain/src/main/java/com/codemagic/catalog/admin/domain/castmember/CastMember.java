package com.codemagic.catalog.admin.domain.castmember;

import com.codemagic.catalog.admin.domain.AggregateRoot;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.util.InstantUtil;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    private CastMember(final CastMemberID memberID,
                       final String name,
                       final CastMemberType type,
                       final Instant createdAt,
                       final Instant updatedAt) {
        super(memberID);
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        selfValidate();
    }

    public static CastMember with(final CastMember member) {
        return new CastMember(member.getId(),
                member.getName(),
                member.getType(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

    public static CastMember newMember(final String name, final CastMemberType type) {
        final var id = CastMemberID.unique();
        final var now = InstantUtil.now();
        return new CastMember(id, name, type, now, now);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException(notification.getErrors());
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
