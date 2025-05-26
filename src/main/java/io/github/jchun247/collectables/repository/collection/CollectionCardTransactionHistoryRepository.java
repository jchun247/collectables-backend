package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.dto.collection.CollectionCardQuantity;
import io.github.jchun247.collectables.model.collection.CollectionCardTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionCardTransactionHistoryRepository extends JpaRepository<CollectionCardTransactionHistory, Long> {
    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM CollectionCardTransactionHistory t WHERE t.collectionCard.id = :collectionCardId")
    int sumQuantityByCollectionCardId(@Param("collectionCardId") Long collectionCardId);

    Page<CollectionCardTransactionHistory> findTransactionHistoriesByCollectionCardId(
            Long collectionCardId,
            Pageable pageable
    );

    long countByCollectionCardId(Long collectionCardId);

    /**
     * Calculates the sum of quantities for a given list of CollectionCard IDs
     * and returns the result as a list of DTOs.
     */
    @Query("""
        SELECT new io.github.jchun247.collectables.dto.collection.CollectionCardQuantity(
            ccth.collectionCard.id,
            SUM(ccth.quantity)
        )
        FROM CollectionCardTransactionHistory ccth
        WHERE ccth.collectionCard.id IN :collectionCardIds
        GROUP BY ccth.collectionCard.id
    """)
    List<CollectionCardQuantity> sumQuantitiesByCollectionCardIds(@Param("collectionCardIds") List<Long> collectionCardIds);
}
