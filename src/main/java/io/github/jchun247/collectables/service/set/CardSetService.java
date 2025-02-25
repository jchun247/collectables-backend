package io.github.jchun247.collectables.service.set;

import io.github.jchun247.collectables.dto.card.CardSetDto;
import io.github.jchun247.collectables.model.card.CardSeries;
import io.github.jchun247.collectables.model.card.CardSet;

import java.util.List;

public interface CardSetService {
    List<CardSet> getCardSetsBySeries(CardSeries series);
    List<CardSeries> getCardSeries();
}
