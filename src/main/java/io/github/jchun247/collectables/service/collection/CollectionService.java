package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.model.collection.CollectionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionService {
    CollectionListDTO createCollectionList(CreateCollectionListDTO createCollectionListDTO);
    PortfolioDTO createPortfolio(CreatePortfolioDTO createPortfolioDTO);
    CollectionDTO updateCollectionDetails(Long collectionId, UpdateCollectionDTO updateCollectionDTO);
    void deleteCollection(Long collectionId);
    CollectionCardDTO addCardToCollection(Long collectionId, AddCardToCollectionDTO addCardToCollectionDTO);
    void deleteCardFromCollection(Long collectionId, Long collectionCardId, int quantityToRemove);
    CollectionDTO getCollectionDetails(Long collectionId);
    Page<CollectionCardDTO> getCollectionCards(Long collectionId, Pageable pageable);
    Page<PortfolioValueHistoryDTO> getPortfolioValueHistory(Long portfolioId, Pageable pageable);
    Page<CollectionCardTransactionHistoryDTO> getCollectionCardTransactionHistory(Long collectionId, Long collectionCardId, Pageable pageable);
    CollectionCardTransactionHistoryDTO updateTransactionDetails(Long collectionId, Long transactionId, UpdateTransactionDTO updateTransactionDTO);
    void deleteTransaction(Long collectionId, Long transactionId);
    Page<CollectionDTO> getCollectionsByUserId(String targetUserAuth0Id, CollectionType type, Pageable pageable);
    void updateAllCollections();
}
