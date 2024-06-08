package com.codemagic.catalog.admin.application.castmember.update;

import com.codemagic.catalog.admin.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
