package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.portfolio.AddCardRequestDto;
import io.github.jchun247.collectables.dto.portfolio.CreateCollectionDto;
import io.github.jchun247.collectables.dto.portfolio.CollectionDto;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import io.github.jchun247.collectables.service.collection.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    // TODO: might need to have pathvariable for id?
    @GetMapping("/history")
    public List<CollectionValueHistory> getPortfolioValueHistory(@RequestParam Long portfolioId) {
        return collectionService.getPortfolioValueHistory(portfolioId);
    }

    // TODO: change method to return a DTO
    @PostMapping("/addCard")
    public void addCardToPortfolio(@RequestBody @Valid AddCardRequestDto request) {
        collectionService.addCardToPortfolio(
                request.getPortfolioId(),
                request.getCardId(),
                request.getCondition(),
                request.getQuantity()
        );
    }

    @PostMapping("/create")
    public CollectionDto createPortfolio(@RequestBody CreateCollectionDto createCollectionDto) {
        return collectionService.createPortfolio(createCollectionDto);
    }
}
