package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardSuperType {
    POKEMON("Pokémon"),
    TRAINER("Trainer"),
    ENERGY("Energy");

    private final String databaseValue;

    CardSuperType(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static CardSuperType fromDatabaseValue(String value) {
        if (value == null) return null;
        for (CardSuperType superType : values()) {
            if (superType.getDatabaseValue().equals(value)) {
                return superType;
            }
        }
        throw new IllegalArgumentException("No CardType found for value: " + value);
    }
}
