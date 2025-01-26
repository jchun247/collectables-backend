package io.github.jchun247.collectables.model;

import lombok.Data;

import java.util.List;

@Data
public class CreateCardRequest {
    private String name;
    private String game;
    private Long set;
    private String setNumber;
    private CardRarity rarity;
    private List<CreateCardPriceRequest> prices;
}
