package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.collection.AddCardRequestDto;
import io.github.jchun247.collectables.dto.collection.CreateCollectionDto;
import io.github.jchun247.collectables.dto.collection.CollectionDto;
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
    public List<CollectionValueHistory> getCollectionValueHistory(@RequestParam Long collectionId) {
        return collectionService.getCollectionValueHistory(collectionId);
    }

    // TODO: change method to return a DTO
    @PostMapping("/addCard")
    public void addCardToCollection(@RequestBody @Valid AddCardRequestDto request) {
        collectionService.addCardToCollection(
                request.getCollectionId(),
                request.getCardId(),
                request.getCondition(),
                request.getQuantity()
        );
    }

    @PostMapping("/create")
    public CollectionDto createCollection(@RequestBody CreateCollectionDto createCollectionDto) {
        return collectionService.createCollection(createCollectionDto);
    }
}
