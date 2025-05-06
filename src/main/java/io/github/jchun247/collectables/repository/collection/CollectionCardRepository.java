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

    @Query("SELECT new io.github.jchun247.collectables.dto.collection.CollectionCardDTO(" +
    "cc.id, cc.collection.id, cc.card.id, cc.condition, cc.quantity, cc.purchaseDate, cc.costBasis " +
    ") " +
    "FROM CollectionCard cc " +
    "JOIN cc.collection col " +
    "JOIN cc.card c " +
    "WHERE col.id = :collectionId")
    Page<CollectionCardDTO> findAsDtoByCollectionId(@Param("collectionId") Long collectionId, Pageable pageable);
}