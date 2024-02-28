package com.codemagic.catalog.admin.infrastructure.configuration.usecases;

import com.codemagic.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.create.DefaultCreateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.delete.DefaultDeleteCategoryUseCase;
import com.codemagic.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.get.DetaultGetCategoryByIdUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.codemagic.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.codemagic.catalog.admin.application.category.update.DefaultUpdateCategoryUseCase;
import com.codemagic.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway gateway;

    public CategoryUseCaseConfig(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(gateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(gateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(gateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DetaultGetCategoryByIdUseCase(gateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(gateway);
    }
}
