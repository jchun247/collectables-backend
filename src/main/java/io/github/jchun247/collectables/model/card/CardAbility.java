package io.github.jchun247.collectables.model.card;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CardAbility {
    private String name;
    private String text;
    private String type;
}
