package io.github.jchun247.collectables.service.card;

import io.github.jchun247.collectables.dto.card.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.model.card.*;

public interface CardService {
    CardDto createCard(CreateCardRequest cardRequest);
    PagedResponse<CardDto> getCards(int page, int size, CardGame game,
                                             String setCode, CardRarity rarity, CardCondition condition,
                                             String sortOption, Double minPrice, Double maxPrice);
    Card getCardById(Long id);
}
