package io.github.jchun247.collectables.service.user;

import io.github.jchun247.collectables.model.user.UserPortfolio;
import io.github.jchun247.collectables.model.user.UserPortfolioValueHistory;
import io.github.jchun247.collectables.repository.user.UserPortfolioRepository;
import io.github.jchun247.collectables.repository.user.UserPortfolioValueHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPortfolioServiceImpl implements UserPortfolioService {
    private final UserPortfolioRepository userPortfolioRepository;
    private final UserPortfolioValueHistoryRepository userPortfolioValueHistoryRepository;


    @Override
    public List<UserPortfolioValueHistory> getPortfolioValueHistory(Long portfolioId) {
        return userPortfolioValueHistoryRepository.findAllByPortfolioId(portfolioId);
    }

    @Override
    public void updatePortfolioValue(UserPortfolio portfolio) {
        double currentValue = portfolio.calculateCurrentValue();
        UserPortfolioValueHistory valueHistory = new UserPortfolioValueHistory();
        valueHistory.setPortfolio(portfolio);
        valueHistory.setValue(currentValue);
        valueHistory.setTimestamp(LocalDateTime.now());
        userPortfolioValueHistoryRepository.save(valueHistory);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void updateAllPortfolios() {
        List<UserPortfolio> portfolios = userPortfolioRepository.findAll();
        for (UserPortfolio portfolio : portfolios) {
            updatePortfolioValue(portfolio);
        }
    }
}
