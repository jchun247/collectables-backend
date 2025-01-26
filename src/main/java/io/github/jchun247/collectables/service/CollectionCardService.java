package io.github.jchun247.collectables.service;


import io.github.jchun247.collectables.model.collection.CollectionCard;

import java.util.List;

public interface CollectionCardService {
    CollectionCard addCardToCollection(Long collectionId, Long cardId);
    List<CollectionCard> getCardsInCollection(Long collectionId);
    CollectionCard updateCardQuantity(Long collectionCardId, int newQuantity);
    void removeCardFromCollection(Long collectionCardId);
}
