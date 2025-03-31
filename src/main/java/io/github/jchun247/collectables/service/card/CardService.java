package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CardDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    PagedResponse<BasicCardDTO> getCards(int page, int size, List<CardGame> games,
                                         String setId, CardRarity rarity, CardCondition condition,
                                         String sortOption, BigDecimal minPrice, BigDecimal maxPrice, String searchQuery);
    BasicCardDTO getCardWithBasicData(Long id);

    CardDTO getCardWithAllData(Long id);
}
