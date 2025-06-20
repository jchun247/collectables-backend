package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CardSearchCriteria {
    private final List<CardGame> games;
    private final String setId;
    private final CardRarity rarity;
    private final CardCondition condition;
    private final CardFinish finish;
    private final String query;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
}