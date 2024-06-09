package com.codemagic.catalog.admin.application.castmember.retrieve.get;

import com.codemagic.catalog.admin.application.UseCase;

public sealed abstract class GetCastMemberByIDUseCase
    extends UseCase<String, CastMemberOutput>
    permits DefaultGetCastMemberByIDUseCase {
}
