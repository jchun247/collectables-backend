package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.model.card.CardPriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPriceHistoryRepository extends JpaRepository<CardPriceHistory, Long> {
    Page<CardPriceHistory> findPriceHistoryByCardId(Long cardId, Pageable pageable);

}
