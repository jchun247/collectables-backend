package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.CollectionCardDTO;
import io.github.jchun247.collectables.dto.collection.CollectionDTO;
import io.github.jchun247.collectables.dto.collection.CreateCollectionDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;

import java.util.List;

public interface CollectionService {
    CollectionCardDTO addCardToCollection(Long collectionId, Long cardId, CardCondition condition, int quantity);
    void deleteCardFromCollection(Long collectionId, Long cardId, CardCondition condition, int quantity);
//    List<CollectionValueHistory> getCollectionValueHistory(Long collectionId);
    CollectionDTO getCollectionDetails(Long collectionId);
    void updateCollectionValue(Collection collection);
    void updateAllCollections();
    CollectionDTO createCollection(CreateCollectionDTO createCollectionDto);
    List<CollectionDTO> getAllCollectionsInfoById(Long userId);
}
