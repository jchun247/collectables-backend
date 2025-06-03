package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.collection.PortfolioValueHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PortfolioValueHistoryRepository extends JpaRepository<PortfolioValueHistory, Long>  {
    Page<PortfolioValueHistory> findByPortfolioIdAndTimestampBetween(
            Long portfolioId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}