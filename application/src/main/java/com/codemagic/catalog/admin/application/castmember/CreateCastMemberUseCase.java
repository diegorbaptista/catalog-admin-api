package com.codemagic.catalog.admin.application.castmember;

import com.codemagic.catalog.admin.application.UseCase;

public abstract sealed class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
    permits DefaultCreateCastMemberUseCase {
}
