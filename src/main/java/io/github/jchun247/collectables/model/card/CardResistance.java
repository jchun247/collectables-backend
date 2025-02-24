package io.github.jchun247.collectables.model.card;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResistance {
    @Enumerated(EnumType.STRING)
    private CardType resistanceType;

    @Enumerated(EnumType.STRING)
    private CardModifier resistanceModifier;

    private int resistanceAmount;
}
