package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
//import io.github.jchun247.collectables.dto.card.CardDto;
import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.card.CardDTO;
import io.github.jchun247.collectables.model.card.*;
import io.github.jchun247.collectables.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

//    @PostMapping("/create")
//    public ResponseEntity<CardDto> createCard(@RequestBody @Valid CreateCardRequestDto cardRequest) {
//        CardDto createdCard = cardService.createCard(cardRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
//    }

//    @GetMapping
//    public ResponseEntity<PagedResponse<CardDto>> getFilteredCards(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "12") int size,
//            @RequestParam(defaultValue = "name") String sortOption,
//            @RequestParam(required = false) List<CardGame> games,
//            @RequestParam(required = false) String setCode,
//            @RequestParam(required = false) CardRarity rarity,
//            @RequestParam(defaultValue = "NEAR_MINT") CardCondition condition,
//            @RequestParam(required = false) Double minPrice,
//            @RequestParam(required = false) Double maxPrice,
//            @RequestParam(required = false) String query
//    ){
//        PagedResponse<CardDto> response = cardService.getCards(page, size, games,
//                setCode, rarity, condition, sortOption, minPrice, maxPrice, query);
//        return ResponseEntity.ok(response);
//        return null;
//    }

    @GetMapping("/{id}/basic")
    public ResponseEntity<BasicCardDTO> getCardWithBasicData(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardWithBasicData(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardWithAllData(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardWithAllData(id));
    }

}
