package com.codemagic.catalog.admin.application.category.update;

import com.codemagic.catalog.admin.application.UseCase;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
