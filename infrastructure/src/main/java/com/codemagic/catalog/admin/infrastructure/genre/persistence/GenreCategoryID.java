package com.codemagic.catalog.admin.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(name = "genre_id", nullable = false)
    private String genreID;

    @Column(name = "category_id", nullable = false)
    private String categoryID;

    public GenreCategoryID() {}

    private GenreCategoryID(final String genreID, final String categoryID) {
        this.genreID = genreID;
        this.categoryID = categoryID;
    }

    public static GenreCategoryID from(final String genreID, final String categoryID) {
        return new GenreCategoryID(genreID, categoryID);
    }

    public String getGenreID() {
        return genreID;
    }

    public void setGenreID(String genreID) {
        this.genreID = genreID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(genreID, that.genreID) && Objects.equals(categoryID, that.categoryID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreID, categoryID);
    }

}
