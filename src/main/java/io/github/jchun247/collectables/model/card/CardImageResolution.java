package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardImageResolution {
    LOW_RES("small"),
    HIGH_RES("large");

    private final String databaseValue;

    CardImageResolution(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static CardImageResolution fromDatabaseValue(String value) {
        if (value == null) return null;
        for (CardImageResolution resolution : values()) {
            if (resolution.getDatabaseValue().equals(value)) {
                return resolution;
            }
        }
        throw new IllegalArgumentException("No CardImageResolution found for value: " + value);
    }
}
