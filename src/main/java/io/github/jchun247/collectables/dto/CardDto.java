package io.github.jchun247.collectables.dto;

import io.github.jchun247.collectables.model.Card;
import io.github.jchun247.collectables.model.CardRarity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CardDto {
//    private Long id;
    private String name;
    private String game;
    private Long set;
    private String setNumber;
    private CardRarity rarity;
    private List<CardPriceDto> prices;

    public CardDto(Card card) {
//        this.id = card.getId();
        this.name = card.getName();
        this.game = card.getGame();
        this.set = card.getSet();
        this.setNumber = card.getSetNumber();
        this.rarity = card.getRarity();
        this.prices = card.getPrices().stream()
                .map(CardPriceDto::new)
                .collect(Collectors.toList());
    }

}
