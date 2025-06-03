package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.PagedResponse;
import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.model.collection.CollectionType;
import io.github.jchun247.collectables.service.collection.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @PostMapping("/lists")
    public ResponseEntity<CollectionListDTO> createList(@RequestBody CreateCollectionListDTO createCollectionListDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collectionService.createCollectionList(createCollectionListDTO));
    }

    @PostMapping("/portfolios")
    public ResponseEntity<PortfolioDTO> createPortfolio(@RequestBody CreatePortfolioDTO createPortfolioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collectionService.createPortfolio(createPortfolioDTO));
    }

    @PatchMapping("/{collectionId}")
    public ResponseEntity<CollectionDTO> updateCollectionDetails(
            @PathVariable Long collectionId,
            @RequestBody @Valid UpdateCollectionDTO updateCollectionDTO) {
        return ResponseEntity.ok(collectionService.updateCollectionDetails(collectionId, updateCollectionDTO));
    }

    @DeleteMapping("/{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(@PathVariable Long collectionId) {
        collectionService.deleteCollection(collectionId);
    }

    @PostMapping("/{collectionId}/cards")
    public CollectionCardDTO addCardToCollection(
            @PathVariable Long collectionId,
            @RequestBody @Valid AddCardToCollectionDTO addCardToCollectionDTO) {
        return collectionService.addCardToCollection(collectionId, addCardToCollectionDTO);
    }

    @DeleteMapping("/{collectionId}/cards/{collectionCardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCardFromCollection(
            @PathVariable Long collectionId,
            @PathVariable Long collectionCardId) {
        collectionService.deleteCardFromCollection(collectionId, collectionCardId);
    }

    @GetMapping("/{collectionId}")
    public CollectionDTO getCollectionDetails(@PathVariable Long collectionId) {
        return collectionService.getCollectionDetails(collectionId);
    }

    @GetMapping("/{collectionId}/cards/{collectionCardId}")
    public CollectionCardDTO getCollectionCardDetails(
            @PathVariable Long collectionId,
            @PathVariable Long collectionCardId) {
        return collectionService.getCollectionCardDetails(collectionId, collectionCardId);
    }

    @GetMapping("/{collectionId}/cards")
    public PagedResponse<CollectionCardDTO> getCollectionCards(
            @PathVariable Long collectionId,
            @RequestParam(name = "cardName", required = false) String cardName,
            @PageableDefault(size = 12, sort = "calculatedTotalStackValue", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CollectionCardDTO> page = collectionService.getCollectionCards(collectionId, cardName, pageable);
        return new PagedResponse<>(page);
    }

    @GetMapping("/{collectionId}/value-history")
    public PagedResponse<PortfolioValueHistoryDTO> getPortfolioValueHistory(
            @PathVariable Long collectionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<PortfolioValueHistoryDTO> page = collectionService.getPortfolioValueHistory(
                collectionId, startDate, endDate, pageable);
        return new PagedResponse<>(page);
    }

    @PostMapping("/{collectionId}/cards/{collectionCardId}/transactions")
    public ResponseEntity<CollectionCardTransactionHistoryDTO> addTransaction(
            @PathVariable Long collectionId,
            @PathVariable Long collectionCardId,
            @RequestBody @Valid CreateTransactionDTO createTransactionDTO) {
        CollectionCardTransactionHistoryDTO transaction = collectionService.addTransaction(collectionId, collectionCardId, createTransactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PatchMapping("/{collectionId}/transactions/{transactionId}")
    public ResponseEntity<CollectionCardTransactionHistoryDTO> updateTransaction(
            @PathVariable Long collectionId,
            @PathVariable Long transactionId,
            @RequestBody @Valid UpdateTransactionDTO transactionDTO) {
        return ResponseEntity.ok(collectionService.updateTransactionDetails(collectionId, transactionId, transactionDTO));
    }

    @DeleteMapping("/{collectionId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(
            @PathVariable Long collectionId,
            @PathVariable Long transactionId) {
        collectionService.deleteTransaction(collectionId, transactionId);
    }

    @GetMapping("/{collectionId}/cards/{collectionCardId}/transaction-history")
    public PagedResponse<CollectionCardTransactionHistoryDTO> getCollectionCardTransactionHistory(
            @PathVariable Long collectionId,
            @PathVariable Long collectionCardId,
            Pageable pageable) {
        Page<CollectionCardTransactionHistoryDTO> page = collectionService.getCollectionCardTransactionHistory(
                collectionId, collectionCardId, pageable);
        return new PagedResponse<>(page);
    }

    @GetMapping("/users/{auth0UserId}")
    public PagedResponse<CollectionDTO> getCollectionsByUserId(
            @PathVariable String auth0UserId,
            @RequestParam(name = "type", required = false) CollectionType collectionType,
            Pageable pageable) {
        Page<CollectionDTO> page = collectionService.getCollectionsByUserId(auth0UserId, collectionType, pageable);
        return new PagedResponse<>(page);
    }
}
