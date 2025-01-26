package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.dto.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.model.*;
import io.github.jchun247.collectables.model.card.*;

public interface CardService {
    CardDto createCard(CreateCardRequest cardRequest);
    PagedResponse<CardDto> getCards(int page, int size, String[] sort, CardGame game,
                                    CardSet set, CardRarity rarity, CardCondition condition,
                                    Double minPrice, Double maxPrice);
    Card getCardById(Long id);
}
