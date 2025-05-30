package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.model.card.CardPriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CardPriceHistoryRepository extends JpaRepository<CardPriceHistory, Long> {
    Page<CardPriceHistory> findByCardIdAndTimestampBetween(
            Long cardId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);
}
