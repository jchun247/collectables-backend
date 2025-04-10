package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardSuperType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardSuperTypeConverter implements AttributeConverter<CardSuperType, String> {
    public String convertToDatabaseColumn(CardSuperType attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    public CardSuperType convertToEntityAttribute(String dbData) {
        return dbData != null ? CardSuperType.fromDatabaseValue(dbData) : null;
    }
}
