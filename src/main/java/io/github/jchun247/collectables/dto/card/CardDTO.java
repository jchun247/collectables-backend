package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.*;
import lombok.Data;

import java.util.Set;

@Data
public class CardDTO {
    private Long id;
    private String name;
    private CardGame game;
    private CardVariantGroup variantGroup;
    private String setName;
    private String setNumber;
    private CardRarity rarity;
    private String illustratorName;
    private String flavourText;
    private int hitPoints;
    private int retreatCost;
    private CardWeakness weakness;
    private CardResistance resistance;
    private Set<CardTypesDTO> types;
    private Set<CardAttackDTO> attacks;
    private Set<CardPriceDTO> prices;
    private Set<CardPriceHistoryDTO> priceHistory;
    private Set<CardAbility> abilities;
    private Set<CardSubType> subTypes;
    private Set<CardImage> images;
}
