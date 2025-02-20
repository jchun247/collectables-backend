package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CreateCardRequestDto;
import io.github.jchun247.collectables.model.card.*;

import java.util.List;

public interface CardService {
    CardDto createCard(CreateCardRequestDto cardRequest);
    PagedResponse<CardDto> getCards(int page, int size, List<CardGame> games,
                                     String setCode, CardRarity rarity, CardCondition condition,
                                     String sortOption, Double minPrice, Double maxPrice, String query);
    Card getCardById(Long id);
}
