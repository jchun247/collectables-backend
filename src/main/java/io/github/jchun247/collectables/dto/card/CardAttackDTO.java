package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardEnergy;
import lombok.Data;

import java.util.List;

@Data
public class CardAttackDTO {
    private String name;
    private String text;
    private String damage;
    private List<CardEnergy> cost;
}
