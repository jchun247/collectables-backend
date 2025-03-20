package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardImage;
import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@Data
//@Builder
//public class CardDto {
//    private Long id;
//    private String name;
//    private CardGame game;
//    private String setCode;
//    private String setNumber;
//    private CardRarity rarity;
//    private List<CardPriceDTO> prices;
//    private Set<CardImage> images;
//
//    public static CardDto fromEntity(Card card) {
//        return CardDto.builder()
//                .id(card.getId())
//                .name(card.getName())
//                .game(card.getGame())
//                .setCode(card.getSet().getCode())
//                .setNumber(card.getSetNumber())
//                .rarity(card.getRarity())
//                .prices(card.getPrices().stream()
//                        .map(CardPriceDTO::new)
//                        .collect(Collectors.toList()))
//                .images(card.getImages())
//                .build();
//    }
//
//}
