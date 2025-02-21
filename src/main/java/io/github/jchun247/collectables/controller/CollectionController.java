package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import io.github.jchun247.collectables.service.collection.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @PostMapping("/create")
    public CollectionDto createCollection(@RequestBody CreateCollectionDto createCollectionDto) {
        return collectionService.createCollection(createCollectionDto);
    }

    @PostMapping("/addCard")
    public CollectionCardDto addCardToCollection(@RequestBody @Valid AddCardRequestDto request) {
        return collectionService.addCardToCollection(
                request.getCollectionId(),
                request.getCardId(),
                request.getCondition(),
                request.getQuantity()
        );
    }

    @DeleteMapping("/{collectionId}/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCardsFromCollection(@PathVariable Long collectionId,
                                          @PathVariable Long cardId,
                                          @RequestBody @Valid RemoveCardRequestDto request) {
        collectionService.deleteCardFromCollection(collectionId, cardId, request.getCondition(), request.getQuantity());
    }

    @GetMapping("/{collectionId}/details")
    public CollectionDto getCollectionDetails(@PathVariable Long collectionId) {
        return collectionService.getCollectionDetails(collectionId);
    }

    @GetMapping("/{collectionId}/details/history")
    public ResponseEntity<CollectionValueHistoryResponse> getCollectionValueHistory(@PathVariable Long collectionId) {
        List<CollectionValueHistory> history = collectionService.getCollectionValueHistory(collectionId);
        CollectionValueHistoryResponse response = new CollectionValueHistoryResponse(history);
        return ResponseEntity.ok(response);
    }
}
