package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.dto.collection.CollectionCardDTO;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface CollectionCardRepository extends JpaRepository<CollectionCard, Long> {
    Optional<CollectionCard> findByCollectionIdAndCardIdAndConditionAndCostBasisAndPurchaseDate(Long collectionId, Long cardId, CardCondition condition, BigDecimal costBasis, LocalDate purchaseDate);
    Page<CollectionCard> findByCollectionId(Long collectionId, Pageable pageable);

    @Query("SELECT cc FROM CollectionCard cc " +
            "LEFT JOIN FETCH cc.card c " +               // Fetch the Card entity
            "LEFT JOIN FETCH c.set cs " +          // Fetch CardSet associated with Card
            "LEFT JOIN FETCH c.images img " +          // Fetch images associated with Card
            "LEFT JOIN FETCH c.prices p " +            // Fetch prices associated with Card
            "WHERE cc.collection.id = :collectionId")
    Page<CollectionCard> findDetailedByCollectionId(@Param("collectionId") Long collectionId, Pageable pageable);
}