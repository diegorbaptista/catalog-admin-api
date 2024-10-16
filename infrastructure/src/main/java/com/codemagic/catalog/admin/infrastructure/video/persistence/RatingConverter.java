package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.video.Rating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(final Rating rating) {
        if (rating == null) return null;
        return rating.name();
    }

    @Override
    public Rating convertToEntityAttribute(final String data) {
        if (data == null) return null;
        return Rating.of(data).orElse(null);
    }
}
