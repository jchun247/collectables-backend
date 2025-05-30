package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.dto.collection.AverageCostInfo;
import io.github.jchun247.collectables.model.collection.CollectionCardTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionCardTransactionHistoryRepository extends JpaRepository<CollectionCardTransactionHistory, Long> {
    /**
     * Calculates the sum of quantities for a given CollectionCard ID.
     */
    @Query("""
        SELECT COALESCE(SUM(CASE WHEN ccth.transactionType = 'BUY' THEN ccth.quantity ELSE -ccth.quantity END), 0)
        FROM CollectionCardTransactionHistory ccth
        WHERE ccth.collectionCard.id = :collectionCardId
    """)
    int sumQuantityByCollectionCardId(@Param("collectionCardId") Long collectionCardId);

    /**
     * Calculates all financial statistics for a given list of CollectionCard IDs in a single query.
     * Returns a raw Object array to be mapped in the service layer due to query complexity.
     */
    @Query("""
        SELECT
            ccth.collectionCard.id,
            COALESCE(SUM(CASE WHEN ccth.transactionType = 'BUY' THEN ccth.quantity ELSE -ccth.quantity END), 0L),
            COALESCE(SUM((CASE WHEN ccth.transactionType = 'BUY' THEN ccth.quantity ELSE -ccth.quantity END) * cp.price), 0.0),
            COALESCE(SUM(CASE WHEN ccth.transactionType = 'BUY' THEN (ccth.quantity * ccth.costBasis) ELSE 0 END), 0.0),
            COALESCE(SUM(CASE WHEN ccth.transactionType = 'SELL' THEN (ccth.quantity * ccth.costBasis) ELSE 0 END), 0.0),
            COALESCE(SUM(ccth.realizedGain), 0.0)
        FROM CollectionCardTransactionHistory ccth
        JOIN ccth.collectionCard cc
        LEFT JOIN cc.card c ON c.id = cc.card.id
        LEFT JOIN c.prices cp ON cp.condition = cc.condition AND cp.finish = cc.finish
        WHERE ccth.collectionCard.id IN :collectionCardIds
        GROUP BY ccth.collectionCard.id
    """)
    // Note the change in return type here
    List<Object[]> getBulkStatsForCollectionCards(@Param("collectionCardIds") List<Long> collectionCardIds);

    @Query("""
        SELECT new io.github.jchun247.collectables.dto.collection.AverageCostInfo(
            COALESCE(SUM(ccth.quantity * ccth.costBasis), 0.0),
            COALESCE(SUM(ccth.quantity), 0)
        )
        FROM CollectionCardTransactionHistory ccth
        WHERE ccth.collectionCard.id = :collectionCardId AND ccth.transactionType = 'BUY'
    """)
    AverageCostInfo getAverageCostForBuys(@Param("collectionCardId") Long collectionCardId);

    Page<CollectionCardTransactionHistory> findTransactionHistoriesByCollectionCardId(
            Long collectionCardId,
            Pageable pageable
    );

    long countByCollectionCardId(Long collectionCardId);

    List<CollectionCardTransactionHistory> findAllByCollectionCardId(Long collectionCardId);
}