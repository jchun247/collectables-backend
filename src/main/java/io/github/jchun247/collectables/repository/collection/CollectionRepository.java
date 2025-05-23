package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionType;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    @Query("SELECT COALESCE(SUM(cp.price * cc.quantity), 0.0) " +
            "FROM Collection c " + // Query from base Collection
            "JOIN c.cards cc " +
            "JOIN cc.card card " +
            // LEFT JOIN in case a price is missing for a specific condition
            "LEFT JOIN card.prices cp ON cp.card = card AND cp.condition = cc.condition " +
            "WHERE c.id = :collectionId")
    BigDecimal calculateCollectionValue(@Param("collectionId") Long collectionId);

    @Query("SELECT COALESCE(SUM(cc.quantity), 0) " +
            "FROM Collection c " + // Query from base Collection
            "JOIN c.cards cc " +
            "WHERE c.id = :collectionId")
    int calculateCollectionSize(@Param("collectionId") Long collectionId);

    @Query("SELECT COALESCE(SUM(ccth.costBasis * ccth.quantity), 0.0) " +
            "FROM Portfolio p " + // Query specifically from Portfolio
            "JOIN p.cards cc " +
            "JOIN cc.transactionHistories ccth " +
            "WHERE p.id = :portfolioId")
    BigDecimal calculateCollectionTotalCostBasis(@Param("portfolioId") Long portfolioId);

    // --- Used for getting all collections for a specific user ---
    @Query("SELECT c FROM Collection c " +
            "WHERE c.user.auth0Id = :targetUserAuth0Id " +
            "AND (c.isPublic = true OR c.user.auth0Id = :requestingUserAuth0Id) " +
            "AND (:entityClassFilter IS NULL OR TYPE(c) = :entityClassFilter)")
    Page<Collection> findVisibleCollectionsByUserId(
            @Param("targetUserAuth0Id") String targetUserAuth0Id,
            @Param("requestingUserAuth0Id") @Nullable String requestingUserAuth0Id,
            @Param("entityClassFilter") @Nullable Class<? extends Collection> entityClassFilter,
            Pageable pageable);

    // --- Used for updating all collection values in scheduled job ---
    @Query("SELECT c FROM Collection c")
    Page<Collection> findAllCollectionsPaged(Pageable pageable);
}