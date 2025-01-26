package io.github.jchun247.collectables.model;

import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Data;

import java.util.List;

@Data
public class CreateCardRequest {
    private String name;
    private CardGame game;
    private Long set;
    private String setNumber;
    private CardRarity rarity;
    private List<CreateCardPriceRequest> prices;
}
