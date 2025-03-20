package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardPrice;
import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Data;

import java.util.Set;

@Data
public class BasicCardDTO {
    private Long id;
    private String name;
    private String setName;
    private String setNumber;
    private CardRarity rarity;
    private Set<CardPriceDTO> prices;
    private Set<CardImageDTO> images;
}
