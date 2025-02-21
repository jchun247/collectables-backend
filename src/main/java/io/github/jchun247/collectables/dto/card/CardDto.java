package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CardDto {
    private Long id;
    private String name;
    private CardGame game;
    private String setCode;
    private String setNumber;
    private CardRarity rarity;
    private List<CardPriceDto> prices;
    private String imageUrl;

    public CardDto(Card card) {
        this.id = card.getId();
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

    public static CardDto fromEntity(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .name(card.getName())
                .game(card.getGame())
                .setCode(card.getSet().getCode())
                .setNumber(card.getSetNumber())
                .rarity(card.getRarity())
                .prices(card.getPrices().stream()
                        .map(CardPriceDto::new)
                        .collect(Collectors.toList()))
                .imageUrl(card.getImageUrl())
                .build();
    }

}
