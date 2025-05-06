package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionService {
    CollectionListDTO createCollectionList(CreateCollectionListDTO createCollectionListDTO);
    PortfolioDTO createPortfolio(CreatePortfolioDTO createPortfolioDTO);
    void deleteCollection(Long collectionId);
    CollectionCardDTO addCardToCollection(Long collectionId, AddCardToCollectionDTO addCardToCollectionDTO);
    void deleteCardFromCollection(Long collectionId, Long collectionCardId, int quantity);
    CollectionDTO getCollectionDetails(Long collectionId);
    Page<CollectionCardDTO> getCollectionCards(Long collectionId, Pageable pageable);
    Page<PortfolioValueHistoryDTO> getPortfolioValueHistory(Long portfolioId, Pageable pageable);
    Page<CollectionDTO> getCollectionsByUserId(String targetUserAuth0Id, Pageable pageable);
    void updateAllCollections();
}
