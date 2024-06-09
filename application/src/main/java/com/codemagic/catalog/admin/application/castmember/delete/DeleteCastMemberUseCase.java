package com.codemagic.catalog.admin.application.castmember.delete;

import com.codemagic.catalog.admin.application.UnaryUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnaryUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
