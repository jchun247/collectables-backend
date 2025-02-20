package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionCardRepository extends JpaRepository<CollectionCard, Long> {
    Optional<CollectionCard> findByCollectionIdAndCardIdAndCondition(Long collectionId, Long cardId, CardCondition condition);
}
