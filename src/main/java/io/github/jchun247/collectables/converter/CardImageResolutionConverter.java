package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardImageResolution;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardImageResolutionConverter implements AttributeConverter<CardImageResolution, String> {
    @Override
    public String convertToDatabaseColumn(CardImageResolution attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    @Override
    public CardImageResolution convertToEntityAttribute(String dbData) {
        return dbData != null ? CardImageResolution.fromDatabaseValue(dbData) : null;
    }
}
