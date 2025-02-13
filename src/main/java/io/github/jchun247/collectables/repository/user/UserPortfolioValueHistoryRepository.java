package io.github.jchun247.collectables.repository.user;


import io.github.jchun247.collectables.model.user.UserPortfolioValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPortfolioValueHistoryRepository extends JpaRepository<UserPortfolioValueHistory, Long>  {
    List<UserPortfolioValueHistory> findAllByPortfolioId(Long portfolioId);
}
