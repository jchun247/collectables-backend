package io.github.jchun247.collectables.model.card;

import lombok.Data;

import java.util.List;

@Data
public class CreateCardRequest {
    private String name;
    private CardGame game;
    private String setCode;
    private String setNumber;
    private CardRarity rarity;
    private List<CreateCardPriceRequest> prices;
}
