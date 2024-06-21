package com.codemagic.catalog.admin.infrastructure.api.controllers;

import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberCommand;
import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIDUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.codemagic.catalog.admin.application.castmember.update.UpdateCastMemberCommand;
import com.codemagic.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.pagination.SearchQuery;
import com.codemagic.catalog.admin.infrastructure.api.CastMemberAPI;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CastMemberResponse;
import com.codemagic.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codemagic.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codemagic.catalog.admin.infrastructure.castmember.presenters.CastMemberApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final GetCastMemberByIDUseCase getCastMemberByIDUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase,
                                final UpdateCastMemberUseCase updateCastMemberUseCase,
                                final GetCastMemberByIDUseCase getCastMemberByIDUseCase,
                                final DeleteCastMemberUseCase deleteCastMemberUseCase, ListCastMembersUseCase listCastMembersUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.getCastMemberByIDUseCase = Objects.requireNonNull(getCastMemberByIDUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
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
    public ResponseEntity<CastMemberResponse> get(final String id) {
        final var output = this.getCastMemberByIDUseCase.execute(id);
        return ResponseEntity.ok(CastMemberApiPresenter.present(output));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateCastMemberRequest input) {
        final var command = UpdateCastMemberCommand.with(id, input.name(), input.type());
        final var output = this.updateCastMemberUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(int page, int perPage, String terms, String sort, String direction) {
        return listCastMembersUseCase
                .execute(new SearchQuery(page, perPage, terms, sort, direction))
                .map(CastMemberApiPresenter::present) ;
    }

    @Override
    public ResponseEntity<Void> delete(final String id) {
        this.deleteCastMemberUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
