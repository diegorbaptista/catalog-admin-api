package com.codemagic.catalog.admin.infrastructure.configuration;

import com.codemagic.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.codemagic.catalog.admin.application.genre.create.DefaultCreateGenreUseCase;
import com.codemagic.catalog.admin.application.genre.delete.DefaultDeleteGenreUseCase;
import com.codemagic.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.get.DefaultGetGenreByIDUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.get.GetGenreByIDUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.list.DefaultListGenresUseCase;
import com.codemagic.catalog.admin.application.genre.retrieve.list.ListGenresUseCase;
import com.codemagic.catalog.admin.application.genre.update.DefaultUpdateGenreUseCase;
import com.codemagic.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import org.hibernate.sql.Delete;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;


    public GenreUseCaseConfig(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIDUseCase getGenreByIDUseCase() {
        return new DefaultGetGenreByIDUseCase(genreGateway);
    }

    @Bean
    ListGenresUseCase listGenresUseCase() {
        return new DefaultListGenresUseCase(genreGateway);
    }

}
