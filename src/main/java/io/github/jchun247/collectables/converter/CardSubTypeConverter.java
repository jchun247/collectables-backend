package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardSubType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardSubTypeConverter implements AttributeConverter<CardSubType, String> {
    @Override
    public String convertToDatabaseColumn(CardSubType attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    @Override
    public CardSubType convertToEntityAttribute(String dbData) {
        return dbData != null ? CardSubType.fromDatabaseValue(dbData) : null;
    }
}

