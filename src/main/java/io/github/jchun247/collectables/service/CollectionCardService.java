package io.github.jchun247.collectables.service;


import io.github.jchun247.collectables.model.CollectionCard;

import java.util.List;

public interface CollectionCardService {
    public CollectionCard addCardToCollection(Long collectionId, Long cardId);
    public List<CollectionCard> getCardsInCollection(Long collectionId);
    public CollectionCard updateCardQuantity(Long collectionCardId, int newQuantity);
    public void removeCardFromCollection(Long collectionCardId);
}
