package com.codemagic.catalog.admin.application.castmember.create;

import com.codemagic.catalog.admin.application.UseCase;

public abstract sealed class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
    permits DefaultCreateCastMemberUseCase {
}
