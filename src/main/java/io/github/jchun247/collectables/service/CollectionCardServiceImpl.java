package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import io.github.jchun247.collectables.repository.CardRepository;
import io.github.jchun247.collectables.repository.CollectionCardRepository;
import io.github.jchun247.collectables.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionCardServiceImpl implements CollectionCardService {

    private final CollectionCardRepository collectionCardRepository;
    private final CollectionRepository collectionRepository;
    private final CardRepository cardRepository;

    @Override
    public CollectionCard addCardToCollection(Long collectionId, Long cardId) {
        // check that collection and card both exist
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        CollectionCard collectionCard = new CollectionCard();
        collectionCard.setCollection(collection);
        collectionCard.setCard(card);

        return collectionCardRepository.save(collectionCard);
    }

    @Override
    public List<CollectionCard> getCardsInCollection(Long collectionId) {
        // check if collection exists
        collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        return collectionCardRepository.findByCollectionId(collectionId);
    }

    @Override
    public CollectionCard updateCardQuantity(Long collectionCardId, int newQuantity) {
        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("CollectionCard not found with id: " + collectionCardId));

        collectionCard.setQuantity(newQuantity);
        return collectionCardRepository.save(collectionCard);
    }

    @Override
    public void removeCardFromCollection(Long collectionCardId) {
        CollectionCard collectionCard = collectionCardRepository.findById(collectionCardId)
                .orElseThrow(() -> new ResourceNotFoundException("CollectionCard not found with id: " + collectionCardId));

        collectionCardRepository.delete(collectionCard);
    }

}
