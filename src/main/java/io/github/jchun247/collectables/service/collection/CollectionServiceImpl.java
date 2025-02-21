package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.dto.collection.CollectionCardDto;
import io.github.jchun247.collectables.dto.collection.CreateCollectionDto;
import io.github.jchun247.collectables.dto.collection.CollectionDto;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import io.github.jchun247.collectables.model.collection.CollectionValueHistory;
import io.github.jchun247.collectables.repository.card.CardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionCardRepository;
import io.github.jchun247.collectables.repository.collection.CollectionRepository;
import io.github.jchun247.collectables.repository.collection.CollectionValueHistoryRepository;
import io.github.jchun247.collectables.repository.user.UserRepository;
import io.github.jchun247.collectables.security.VerifyCollectionCardAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionCardRepository collectionCardRepository;
    private final CollectionValueHistoryRepository collectionValueHistoryRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;


    @Override
    @Transactional
    public CollectionCardDto addCardToCollection(Long collectionId, Long cardId, CardCondition condition, int quantity) {
        CollectionCard collectionCard = collectionCardRepository.findByCollectionIdAndCardIdAndCondition(collectionId, cardId, condition)
                .orElseGet(() -> {
                    Collection collection = collectionRepository.findById(collectionId)
                            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
                    Card card = cardRepository.findById(cardId)
                            .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

                    CollectionCard newCollectionCard = new CollectionCard();
                    newCollectionCard.setCollection(collection);
                    newCollectionCard.setCard(card);
                    newCollectionCard.setCondition(condition);
                    newCollectionCard.setQuantity(0); // quantity will be updated below
                    return newCollectionCard;
                });

        // Update quantity
        collectionCard.setQuantity(collectionCard.getQuantity() + quantity);

        CollectionCard savedCard = collectionCardRepository.save(collectionCard);
        return CollectionCardDto.fromEntity(savedCard);
    }

    @Override
    @Transactional
    @VerifyCollectionCardAccess
    public void deleteCardFromCollection(Long collectionId, Long cardId, CardCondition condition, int quantity) {
        CollectionCard collectionCard = collectionCardRepository.findByCollectionIdAndCardIdAndCondition(collectionId, cardId, condition)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found in collection"));
        int newCardQuantity = collectionCard.getQuantity() - quantity;
        if (newCardQuantity < 0) {
            throw new IllegalArgumentException("Cannot remove more cards than are in the collection");
        } else if (newCardQuantity == 0) {
            collectionCardRepository.delete(collectionCard);
        } else {
            collectionCard.setQuantity(newCardQuantity);
            collectionCardRepository.save(collectionCard);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionValueHistory> getCollectionValueHistory(Long collectionId) {
        return collectionValueHistoryRepository.findAllByCollectionId(collectionId);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionDto getCollectionDetails(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        collection.setNumProducts(collectionCardRepository.sumQuantityByCollectionId(collectionId));
        return CollectionDto.fromEntity(collection);
    }

    @Override
    public void updateCollectionValue(Collection collection) {
        BigDecimal currentValue = collection.calculateCurrentValue();
        CollectionValueHistory valueHistory = new CollectionValueHistory();
        valueHistory.setCollection(collection);
        valueHistory.setValue(currentValue);
        valueHistory.setTimestamp(LocalDateTime.now());
        collectionValueHistoryRepository.save(valueHistory);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void updateAllCollections() {
        List<Collection> collections = collectionRepository.findAll();
        for (Collection collection : collections) {
            updateCollectionValue(collection);
        }
    }

    @Override
    @Transactional
    public CollectionDto createCollection(CreateCollectionDto createCollectionDto) {
        UserEntity user = userRepository.findByAuth0Id(createCollectionDto.getAuth0Id()).orElseThrow(() ->
                new ResourceNotFoundException("User not found with auth0Id: " + createCollectionDto.getAuth0Id()));

        Collection collection = Collection.builder()
                .name(createCollectionDto.getName() != null ? createCollectionDto.getName() : "New Collection")
                .description(createCollectionDto.getDescription() != null ? createCollectionDto.getDescription() : "")
                .isPublic(createCollectionDto.isPublic())
                .numProducts(0)
                .user(user)
                .build();

        collectionRepository.save(collection);
        return CollectionDto.fromEntity(collection);
    }
}
