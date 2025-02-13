package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.user.UserPortfolioValueHistory;
import io.github.jchun247.collectables.service.user.UserPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class UserPortfolioController {
    private final UserPortfolioService userPortfolioService;

    @GetMapping("/history")
    public List<UserPortfolioValueHistory> getPortfolioValueHistory(@RequestParam Long portfolioId) {
        return userPortfolioService.getPortfolioValueHistory(portfolioId);
    }
}
