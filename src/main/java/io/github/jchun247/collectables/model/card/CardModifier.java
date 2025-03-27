package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardModifier {
    MULTIPLY("×"),
    ADD("+"),
    REDUCE("-"),
    PERCENTAGE("%");

    private final String databaseValue;

    CardModifier(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static CardModifier fromDatabaseValue(String value) {
        if (value == null) return null;
        for (CardModifier modifier : values()) {
            if (modifier.getDatabaseValue().equals(value)) {
                return modifier;
            }
        }
        throw new IllegalArgumentException("No CardModifier found for value: " + value);
    }
}
