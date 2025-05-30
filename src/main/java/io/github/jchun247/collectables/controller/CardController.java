package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CardPriceHistoryDTO;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<PagedResponse<BasicCardDTO>> getFilteredCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "name") String sortOption,
            @RequestParam(required = false) List<CardGame> games,
            @RequestParam(required = false) String setId,
            @RequestParam(required = false) CardRarity rarity,
            @RequestParam(defaultValue = "NEAR_MINT") CardCondition condition,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) CardFinish finish
    ){
        PagedResponse<BasicCardDTO> response = cardService.getCards(page, size, games,
                setId, rarity, condition, sortOption, minPrice, maxPrice, searchQuery, finish);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCardById(
            @PathVariable Long id,
            @RequestParam(name="view", defaultValue = "FULL") CardView view) {
        if (view == CardView.BASIC) {
            return ResponseEntity.ok(cardService.getCardWithBasicData(id));
        } else {
            return ResponseEntity.ok(cardService.getCardWithAllData(id));
        }
    }

    @GetMapping("/set/{setId}")
    public ResponseEntity<PagedResponse<BasicCardDTO>> getCardsBySetId(
            @PathVariable String setId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "name") String sortOption,
            @RequestParam(required = false) CardRarity rarity,
            @RequestParam(defaultValue = "NEAR_MINT") CardCondition condition,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) CardFinish finish){
        PagedResponse<BasicCardDTO> response = cardService.getCards(page, size, null,
                setId, rarity, condition, sortOption, minPrice, maxPrice, searchQuery, finish);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cardId}/price-history")
    public PagedResponse<CardPriceHistoryDTO> getCardPriceHistory(
            @PathVariable Long cardId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<CardPriceHistoryDTO> priceHistoryPage = cardService.getCardPriceHistory(cardId, startDate, endDate, pageable);
        return new PagedResponse<>(priceHistoryPage);
    }
}
