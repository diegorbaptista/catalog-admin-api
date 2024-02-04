package com.codemagic.catalog.admin.application.category.create;

import com.codemagic.catalog.admin.application.UseCase;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
