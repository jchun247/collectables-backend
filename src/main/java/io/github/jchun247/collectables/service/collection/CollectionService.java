package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.portfolio.CreateCollectionDto;
import io.github.jchun247.collectables.dto.portfolio.CollectionDto;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;

import java.util.List;

public interface CollectionService {
    void addCardToPortfolio(Long portfolioId, Long cardId, CardCondition condition, int quantity);
    List<CollectionValueHistory> getPortfolioValueHistory(Long portfolioId);
    void updatePortfolioValue(Collection portfolio);
    void updateAllPortfolios();
    CollectionDto createPortfolio(CreateCollectionDto createCollectionDto);
}
