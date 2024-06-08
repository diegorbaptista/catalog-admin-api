package com.codemagic.catalog.admin.application.castmember.update;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.exceptions.NotFoundException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;

import java.util.Objects;

public final class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand command) {
        final var memberId = command.id();
        final var memberName = command.name();
        final var memberType = command.type();

        final var member = this.gateway.findById(command.id())
                .orElseThrow(() -> NotFoundException.with(CastMember.class, CastMemberID.from(memberId)));

        final var notification = Notification.create();
        notification.validate(()-> member.update(memberName, memberType));

        if (notification.hasErrors()) {
            notify(notification);
        }

        return UpdateCastMemberOutput.from(this.gateway.update(member));
    }

    private void notify(Notification notification) {
        throw new NotificationException(notification.getErrors());
    }
}
