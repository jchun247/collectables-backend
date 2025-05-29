package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.dto.collection.PortfolioStatBuildingBlocks;
import io.github.jchun247.collectables.model.collection.Collection;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    @Query("""
        SELECT new io.github.jchun247.collectables.dto.collection.PortfolioStatBuildingBlocks(
            cc.collection.id,
            COALESCE(SUM(CASE WHEN t.transactionType = 'BUY' THEN t.quantity ELSE -t.quantity END), 0),
            COALESCE(SUM((CASE WHEN t.transactionType = 'BUY' THEN t.quantity ELSE -t.quantity END) * cp.price), 0.0),
            COALESCE(SUM(CASE WHEN t.transactionType = 'BUY' THEN (t.quantity * t.costBasis) ELSE 0 END), 0.0),
            COALESCE(SUM(CASE WHEN t.transactionType = 'SELL' THEN (t.quantity * t.costBasis) ELSE 0 END), 0.0),
            COALESCE(SUM(t.realizedGain), 0.0)
        )
        FROM CollectionCardTransactionHistory t
        JOIN t.collectionCard cc
        LEFT JOIN cc.card c
        LEFT JOIN c.prices cp ON cp.condition = cc.condition AND cp.finish = cc.finish
        WHERE cc.collection.id IN :collectionIds
        GROUP BY cc.collection.id
    """)
    Set<PortfolioStatBuildingBlocks> getBulkStatBuildingBlocks(@Param("collectionIds") List<Long> collectionIds);

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