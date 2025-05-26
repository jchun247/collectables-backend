package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.dto.collection.CollectionDTO;
import io.github.jchun247.collectables.dto.collection.CollectionStats;
import io.github.jchun247.collectables.model.collection.Collection;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    @Query("""
        SELECT COALESCE(SUM(t.quantity * cp.price), 0.0)
        FROM CollectionCardTransactionHistory t
        JOIN t.collectionCard cc
        JOIN cc.card c
        JOIN c.prices cp
        WHERE cc.collection.id = :collectionId
          AND cp.condition = cc.condition
          AND cp.finish = cc.finish
    """)
    BigDecimal calculateCollectionValue(@Param("collectionId") Long collectionId);

    @Query("SELECT COALESCE(SUM(ccth.quantity), 0) " +
            "FROM CollectionCardTransactionHistory ccth " +
            "JOIN ccth.collectionCard cc " +
            "WHERE cc.collection.id = :collectionId")
    int calculateCollectionSize(@Param("collectionId") Long collectionId);

    @Query("SELECT COALESCE(SUM(ccth.costBasis * ccth.quantity), 0.0) " +
            "FROM Portfolio p " + // Query specifically from Portfolio
            "JOIN p.cards cc " +
            "JOIN cc.transactionHistories ccth " +
            "WHERE p.id = :portfolioId")
    BigDecimal calculateCollectionTotalCostBasis(@Param("portfolioId") Long portfolioId);

    @Query("""
        SELECT new io.github.jchun247.collectables.dto.collection.CollectionStats(
            cc.collection.id,
            COALESCE(SUM(t.quantity), 0),
            COALESCE(SUM(t.quantity * cp.price), 0.0),
            COALESCE(SUM(t.costBasis * t.quantity), 0.0)
        )
        FROM CollectionCardTransactionHistory t
        JOIN t.collectionCard cc
        LEFT JOIN cc.card c
        LEFT JOIN c.prices cp ON cp.condition = cc.condition AND cp.finish = cc.finish
        WHERE cc.collection.id IN :collectionIds
        GROUP BY cc.collection.id
    """)
    Set<CollectionStats> getBulkStatsForCollections(@Param("collectionIds") List<Long> collectionIds);

    @Query("""
        SELECT col
        FROM Collection col
        WHERE col.user.auth0Id = :targetUserAuth0Id
        AND (col.isPublic = true OR col.user.auth0Id = :requestingUserAuth0Id)
        AND (:entityClassFilter IS NULL OR TYPE(col) = :entityClassFilter)
    """)
    Page<Collection> findVisibleCollectionsByUserId(
            @Param("targetUserAuth0Id") String targetUserAuth0Id,
            @Param("requestingUserAuth0Id") String requestingUserAuth0Id,
            @Param("entityClassFilter") @Nullable Class<? extends Collection> entityClassFilter,
            Pageable pageable
    );

    // --- Used for updating all collection values in scheduled job ---
    @Query("SELECT c FROM Collection c")
    Page<Collection> findAllCollectionsPaged(Pageable pageable);
}