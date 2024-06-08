package com.codemagic.catalog.admin.application.castmember.create;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;

import java.util.Objects;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand command) {
        final var name = command.name();
        final var type = command.type();

        final var notification = Notification.create();

        final var castMember = notification.validate(() -> CastMember.newMember(name, type));

        if (notification.hasErrors()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.gateway.create(castMember));
    }

    private void notify(Notification notification) {
        throw new NotificationException(notification.getErrors());
    }

}
