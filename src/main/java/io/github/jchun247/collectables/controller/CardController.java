package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.card.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.service.card.CardService;
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
            @RequestParam(defaultValue = "name") String sortOption,
            @RequestParam(required = false) CardGame game,
            @RequestParam(required = false) String setCode,
            @RequestParam(required = false) CardRarity rarity,
            @RequestParam(defaultValue = "NEAR_MINT") CardCondition condition,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ){
        PagedResponse<CardDto> response = cardService.getCards(page, size, game,
                setCode, rarity, condition, sortOption, minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

}
