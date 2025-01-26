package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.service.collection.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @PostMapping("/create")
    public ResponseEntity<Collection> createCollection(@RequestBody Collection collection) {
        return ResponseEntity.ok(this.collectionService.createCollection(collection));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Collection>> getAllCollections() {
        return ResponseEntity.ok(this.collectionService.getAllCollections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollectionById(@PathVariable Long id) {
        return ResponseEntity.ok(this.collectionService.getCollectionById(id));
    }
}
