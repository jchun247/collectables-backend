package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import io.github.jchun247.collectables.service.collection.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/collections/{collectionId}/history")
    public List<CollectionValueHistory> getCollectionValueHistory(@PathVariable Long collectionId) {
        return collectionService.getCollectionValueHistory(collectionId);
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
                                          @RequestBody RemoveCardRequestDto request) {
        collectionService.deleteCardFromCollection(collectionId, cardId, request.getCondition(), request.getQuantity());
    }

    @PostMapping("/create")
    public CollectionDto createCollection(@RequestBody CreateCollectionDto createCollectionDto) {
        return collectionService.createCollection(createCollectionDto);
    }
}
