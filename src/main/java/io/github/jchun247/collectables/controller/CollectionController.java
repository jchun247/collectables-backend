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
    public CollectionDTO createCollection(@RequestBody CreateCollectionDTO createCollectionDto) {
        return collectionService.createCollection(createCollectionDto);
    }

    @PostMapping("/addCard")
    public CollectionCardDTO addCardToCollection(@RequestBody @Valid AddCardRequestDTO request) {
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
                                          @RequestBody @Valid RemoveCardRequestDTO request) {
        collectionService.deleteCardFromCollection(collectionId, cardId, request.getCondition(), request.getQuantity());
    }

    @GetMapping("/{collectionId}/details")
    public CollectionDTO getCollectionDetails(@PathVariable Long collectionId) {
        return collectionService.getCollectionDetails(collectionId);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<CollectionDTO>> getAllCollectionsInfoById(
            @PathVariable Long userId,
            @RequestParam(required = false) Long requestingUserId) {
        List<CollectionDTO> collections = collectionService.getCollectionsByUserId(userId, requestingUserId);
        return ResponseEntity.ok(collections);
    }
}
