package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.card.CardSeries;
import io.github.jchun247.collectables.model.card.CardSet;
import io.github.jchun247.collectables.service.set.CardSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sets")
@RequiredArgsConstructor
public class CardSetController {
    private final CardSetService cardSetService;

    @GetMapping("/series")
    public ResponseEntity<List<CardSeries>> getAllCardSeries() {
        return ResponseEntity.ok(cardSetService.getAllCardSeries());
    }

    @GetMapping("/by-series")
    public ResponseEntity<List<CardSet>> getCardSetsBySeries(@RequestParam CardSeries series) {
        return ResponseEntity.ok(cardSetService.getCardSetsBySeries(series));
    }
}
