package com.codemagic.catalog.admin.infrastructure.configuration.usecases;

import com.codemagic.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.get.DefaultGetCastMemberByIDUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIDUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.codemagic.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.codemagic.catalog.admin.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.codemagic.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway gateway;

    public CastMemberUseCaseConfig(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(gateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(gateway);
    }

    @Bean
    public GetCastMemberByIDUseCase getCastMemberByIDUseCase() {
        return new DefaultGetCastMemberByIDUseCase(gateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(gateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(gateway);
    }

}

