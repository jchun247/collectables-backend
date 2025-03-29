package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardEnergy {
    COLORLESS("Colorless"),
    DARKNESS("Darkness"),
    FAIRY("Fairy"),
    FIGHTING("Fighting"),
    FIRE("Fire"),
    FREE("Free"),
    FREE_2("FREE"),
    GRASS("Grass"),
    LIGHTNING("Lightning"),
    METAL("Metal"),
    PSYCHIC("Psychic"),
    WATER("Water");

    private final String databaseValue;

    CardEnergy(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static CardEnergy fromDatabaseValue(String value) {
        if (value == null) return null;
        for (CardEnergy energy : values()) {
            if (energy.getDatabaseValue().equals(value)) {
                return energy;
            }
        }
        throw new IllegalArgumentException("No CardEnergy found for value: " + value);
    }
}
