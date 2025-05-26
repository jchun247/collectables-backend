package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.dto.collection.CollectionCardDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionCardRepository extends JpaRepository<CollectionCard, Long> {
    Optional<CollectionCard> findByCollectionIdAndCardIdAndConditionAndFinish(
            Long collectionId,
            Long cardId,
            CardCondition condition,
            CardFinish finish
    );

    /**
     * Finds a page of CollectionCards for a collection and eagerly fetches all ToOne relationships.
     * This avoids N+1 issues for Card, Set, and Collection details.
     */
    @Query("""
        SELECT cc FROM CollectionCard cc
        LEFT JOIN FETCH cc.collection
        LEFT JOIN FETCH cc.card c
        LEFT JOIN FETCH c.set
        LEFT JOIN FETCH c.pokemonDetails
        WHERE cc.collection.id = :collectionId
    """)
    Page<CollectionCard> findPageByCollectionId(@Param("collectionId") Long collectionId, Pageable pageable);
}