package io.github.jchun247.collectables.dto;

import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CardDto {
    private String name;
    private CardGame game;
    private String setCode;
    private String setNumber;
    private CardRarity rarity;
    private List<CardPriceDto> prices;
    private String imageUrl;

    public CardDto(Card card) {
        this.name = card.getName();
        this.game = card.getGame();
        this.setCode = card.getSet().getCode();
        this.setNumber = card.getSetNumber();
        this.rarity = card.getRarity();
        this.prices = card.getPrices().stream()
                .map(CardPriceDto::new)
                .collect(Collectors.toList());
        this.imageUrl = card.getImageUrl();
    }

}
