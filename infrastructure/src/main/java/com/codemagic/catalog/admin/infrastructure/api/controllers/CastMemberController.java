package com.codemagic.catalog.admin.infrastructure.api.controllers;

import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberCommand;
import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.infrastructure.api.CastMemberAPI;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codemagic.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var output = this.createCastMemberUseCase.execute(CreateCastMemberCommand.with(
                input.name(),
                input.type()
        ));
        return ResponseEntity.created(URI.create("/cast-members/".concat(output.id()))).body(output);
    }

    @Override
    public ResponseEntity<CastMemberResponse> get(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateCastMemberRequest input) {
        return null;
    }

    @Override
    public Pagination<CastMemberListResponse> list(int page, int perPage, String terms, String sort, String direction) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        return null;
    }
}
