package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CreateCardRequestDto;
import io.github.jchun247.collectables.model.card.*;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
//    CardDto createCard(CreateCardRequestDto cardRequest);
    PagedResponse<CardDto> getCards(int page, int size, List<CardGame> games,
                                    String setCode, CardRarity rarity, CardCondition condition,
                                    String sortOption, BigDecimal minPrice, BigDecimal maxPrice, String searchQuery);
    Card getCardById(Long id);
}
