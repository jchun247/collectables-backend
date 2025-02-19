package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.CreateCollectionDto;
import io.github.jchun247.collectables.dto.collection.CollectionDto;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;

import java.util.List;

public interface CollectionService {
    void addCardToCollection(Long collectionId, Long cardId, CardCondition condition, int quantity);
    List<CollectionValueHistory> getCollectionValueHistory(Long collectionId);
    void updateCollectionValue(Collection collection);
    void updateAllCollections();
    CollectionDto createCollection(CreateCollectionDto createCollectionDto);
}
