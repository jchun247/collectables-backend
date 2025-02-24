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
public class CardWeakness {
    @Enumerated(EnumType.STRING)
    private CardType weaknessType;

    @Enumerated(EnumType.STRING)
    private CardModifier weaknessModifier;

    private int weaknessAmount;
}
