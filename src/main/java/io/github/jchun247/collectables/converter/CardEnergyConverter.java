package io.github.jchun247.collectables.converter;

import io.github.jchun247.collectables.model.card.CardEnergy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardEnergyConverter implements AttributeConverter<CardEnergy, String> {
    @Override
    public String convertToDatabaseColumn(CardEnergy attribute) {
        return attribute != null ? attribute.getDatabaseValue() : null;
    }

    @Override
    public CardEnergy convertToEntityAttribute(String dbData) {
        return dbData != null ? CardEnergy.fromDatabaseValue(dbData) : null;
    }
}
