package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardModifier {
    MULTIPLY("×"),
    ADD("+"),
    REDUCE("-"),
    PERCENTAGE("%");

    private final String symbol;

    CardModifier(String symbol) {
        this.symbol = symbol;
    }
}
