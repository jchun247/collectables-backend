package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.model.card.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c.id FROM Card c " +
            "LEFT JOIN c.set cs " +
            "WHERE (:games IS NULL OR c.game IN :games) " +
            "AND (:setId IS NULL OR cs.id = :setId) " +
            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR cs.id LIKE %:query% OR c.setNumber LIKE %:query%) " +
            "AND EXISTS (SELECT 1 FROM CardPrice p WHERE p.card = c " +
                "AND (:finish IS NOT NULL AND p.finish = :finish " +
                    "OR :finish IS NULL AND p.finish IN ('NORMAL', 'HOLOFOIL', 'REVERSE_HOLO', 'STAMP') " +
                    "AND NOT EXISTS (SELECT 1 FROM CardPrice p2 WHERE p2.card = c AND " +
                        "((p.finish = 'HOLOFOIL' AND p2.finish = 'NORMAL') OR " +
                        "(p.finish = 'REVERSE_HOLO' AND p2.finish IN ('NORMAL', 'HOLOFOIL')) OR " +
                        "(p.finish = 'STAMP' AND p2.finish IN ('NORMAL', 'HOLOFOIL', 'REVERSE_HOLO')) OR " +
                        "(p.finish NOT IN ('NORMAL', 'HOLOFOIL', 'REVERSE_HOLO', 'STAMP'))))) " +
                "AND (:condition IS NULL OR p.condition = :condition) " +
                "AND p.price BETWEEN :minPrice AND :maxPrice)")
    Page<Long> findCardIdsByFilters(
            @Param("games") List<CardGame> games,
            @Param("setId") String setId,
            @Param("rarity") CardRarity rarity,
            @Param("condition") CardCondition condition,
            @Param("finish") CardFinish finish,
            @Param("query") String query,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    @Query("SELECT DISTINCT c FROM Card c " +
            "LEFT JOIN FETCH c.prices p " +
            "LEFT JOIN FETCH c.images " +
            "LEFT JOIN FETCH c.set " +
            "WHERE c.id IN :ids")
    List<Card> findCardsByIds(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"prices", "images", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithBasicDataById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"types", "attacks", "prices", "priceHistory", "abilities", "subTypes", "images", "variantGroup", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithAllDataById(Long id);
}
