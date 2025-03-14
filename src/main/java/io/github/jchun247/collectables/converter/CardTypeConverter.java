package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardTypeConverter implements AttributeConverter<CardType, String> {
    @Override
    public String convertToDatabaseColumn(CardType attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    @Override
    public CardType convertToEntityAttribute(String dbData) {
        return dbData != null ? CardType.fromDatabaseValue(dbData) : null;
    }
}
