package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.dto.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.model.Card;
import io.github.jchun247.collectables.model.CardCondition;
import io.github.jchun247.collectables.model.CardRarity;
import io.github.jchun247.collectables.model.CreateCardRequest;

import java.util.List;

public interface CardService {
    CardDto createCard(CreateCardRequest cardRequest);
    PagedResponse<CardDto> getCards(int page, int size, String[] sort, String game,
                                    Long set, CardRarity rarity, CardCondition condition,
                                    Double minPrice, Double maxPrice);
    Card getCardById(Long id);
}
