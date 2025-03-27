package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CardDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
//    CardDto createCard(CreateCardRequestDto cardRequest);
//    PagedResponse<CardDto> getCards(int page, int size, List<CardGame> games,
//                                    String setCode, CardRarity rarity, CardCondition condition,
//                                    String sortOption, BigDecimal minPrice, BigDecimal maxPrice, String searchQuery);
    BasicCardDTO getCardWithBasicData(Long id);

    CardDTO getCardWithAllData(Long id);
}
