package io.github.jchun247.collectables.repository.collection;


import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionValueHistoryRepository extends JpaRepository<CollectionValueHistory, Long>  {
    List<CollectionValueHistory> findAllByPortfolioId(Long portfolioId);
}
