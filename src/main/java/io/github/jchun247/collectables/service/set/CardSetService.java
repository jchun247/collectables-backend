package io.github.jchun247.collectables.service.set;

import io.github.jchun247.collectables.dto.card.BasicCardSetDTO;
import io.github.jchun247.collectables.model.card.CardSeries;

import java.util.List;

public interface CardSetService {
    List<BasicCardSetDTO> getCardSetsBySeries(CardSeries series);
    List<CardSeries> getAllCardSeries();
}
