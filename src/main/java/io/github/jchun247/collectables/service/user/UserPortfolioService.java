package io.github.jchun247.collectables.service.user;

import io.github.jchun247.collectables.model.user.UserPortfolio;
import io.github.jchun247.collectables.model.user.UserPortfolioValueHistory;

import java.util.List;

public interface UserPortfolioService {
    List<UserPortfolioValueHistory> getPortfolioValueHistory(Long portfolioId);
    void updatePortfolioValue(UserPortfolio portfolio);
    void updateAllPortfolios();
}
