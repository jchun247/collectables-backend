package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.model.*;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<CardDto> createCard(@RequestBody CreateCardRequest cardRequest) {
        CardDto createdCard = cardService.createCard(cardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CardDto>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name,asc") String [] sort,
            @RequestParam(required = false) CardGame game,
            @RequestParam(required = false) CardSet set,
            @RequestParam(required = false) CardRarity rarity,
            @RequestParam(required = false) CardCondition condition,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ){
        PagedResponse<CardDto> response = cardService.getCards(page, size, sort, game,
                set, rarity, condition, minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }
}
