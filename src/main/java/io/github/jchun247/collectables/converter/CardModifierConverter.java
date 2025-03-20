package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardModifier;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardModifierConverter implements AttributeConverter<CardModifier, String> {
    @Override
    public String convertToDatabaseColumn(CardModifier attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    @Override
    public CardModifier convertToEntityAttribute(String dbData) {
        return dbData != null ? CardModifier.fromDatabaseValue(dbData) : null;
    }
}