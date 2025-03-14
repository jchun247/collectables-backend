package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardRarity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardRarityConverter implements AttributeConverter<CardRarity, String> {
    @Override
    public String convertToDatabaseColumn(CardRarity attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    @Override
    public CardRarity convertToEntityAttribute(String dbData) {
        return dbData != null ? CardRarity.fromDatabaseValue(dbData) : null;
    }
}
