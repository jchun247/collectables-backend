package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardEnergy;
import lombok.Data;

@Data
public class CardAttackCostDTO {
    private Long id;
    private CardEnergy cost;
}
