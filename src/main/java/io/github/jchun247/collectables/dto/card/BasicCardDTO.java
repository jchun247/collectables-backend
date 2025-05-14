package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class BasicCardDTO {
    private Long id;
    private String name;
    private String setId;
    private String setName;
    private String setNumber;
    private CardRarity rarity;
    private Set<CardPriceDTO> prices;
    private String imageUrl;

    // Constructor for JPQL query instantiation
    public BasicCardDTO(Long id, String name, String setId, String setName, String setNumber, CardRarity rarity) {
        this.id = id;
        this.name = name;
        this.setId = setId;
        this.setName = setName;
        this.setNumber = setNumber;
        this.rarity = rarity;
        this.imageUrl = null;
        this.prices = new HashSet<>();
    }
}