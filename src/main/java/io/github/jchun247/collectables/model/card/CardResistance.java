package io.github.jchun247.collectables.model.card;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResistance {
    private CardType resistanceType;
    private CardModifier resistanceModifier;
    private Integer resistanceValue;
}
