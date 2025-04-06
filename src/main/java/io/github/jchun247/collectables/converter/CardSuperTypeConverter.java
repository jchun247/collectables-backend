package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardSuperType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardSuperTypeConverter {
    public String convertToDatabaseColumn(CardSuperType attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    public CardSuperType convertToEntityAttribute(String dbData) {
        return dbData != null ? CardSuperType.fromDatabaseValue(dbData) : null;
    }
}
