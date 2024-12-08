package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.Card;
import io.github.jchun247.collectables.model.Collection;
import io.github.jchun247.collectables.model.CollectionCard;
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
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));



        return null;
    }

    @Override
    public List<CollectionCard> getCardsInCollection(Long collectionId) {
        return List.of();
    }

    @Override
    public CollectionCard updateCardQuantity(Long collectionCardId, int newQuantity) {
        return null;
    }

    @Override
    public void removeCardFromCollection(Long collectionCardId) {

    }

    // Add a card to a collection


}
