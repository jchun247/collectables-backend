package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardType {
    GRASS("Grass"),
    FIRE("Fire"),
    WATER("Water"),
    LIGHTNING("Lightning"),
    FIGHTING("Fighting"),
    PSYCHIC("Psychic"),
    COLORLESS("Colorless"),
    DARKNESS("Darkness"),
    METAL("Metal"),
    DRAGON("Dragon"),
    FAIRY("Fairy");

    private final String databaseValue;

    CardType(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static CardType fromDatabaseValue(String value) {
        if (value == null) return null;
        for (CardType type : values()) {
            if (type.getDatabaseValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No CardType found for value: " + value);
    }
}