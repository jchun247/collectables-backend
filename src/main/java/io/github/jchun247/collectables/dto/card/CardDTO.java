package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.*;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class CardDTO {
    private Long id;
    private String name;
    private CardGame game;
    private CardVariantGroupDTO variantGroup;
    private String setId;
    private String setName;
    private String setNumber;
    private CardRarity rarity;
    private String illustratorName;
    private CardPokemonDetailsDTO pokemonDetails;
    private Set<CardRuleDTO> rules = new LinkedHashSet<>();
    private Set<CardPriceDTO> prices;
    private Set<CardPriceHistoryDTO> priceHistory;
    private Set<CardSubType> subTypes;
    private Set<CardImage> images;
}
