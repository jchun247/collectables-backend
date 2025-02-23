package io.github.jchun247.collectables.model.card;

import jakarta.persistence.Embeddable;

@Embeddable
public class CardAbility {
    private String name;
    private String text;
    private String type;
}
