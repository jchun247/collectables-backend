package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.CollectionCardDto;
import io.github.jchun247.collectables.dto.collection.CreateCollectionDto;
import io.github.jchun247.collectables.dto.collection.CollectionDto;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CollectionService {
    CollectionCardDto addCardToCollection(Long collectionId, Long cardId, CardCondition condition, int quantity);
    void deleteCardFromCollection(Long collectionId, Long cardId, CardCondition condition, int quantity);
    List<CollectionValueHistory> getCollectionValueHistory(Long collectionId);
    CollectionDto getCollectionDetails(Long collectionId);
    void updateCollectionValue(Collection collection);
    void updateAllCollections();
    CollectionDto createCollection(CreateCollectionDto createCollectionDto);
}
