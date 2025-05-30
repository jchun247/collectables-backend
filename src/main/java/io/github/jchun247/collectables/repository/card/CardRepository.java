package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.model.card.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT DISTINCT c.id FROM Card c " +
            "JOIN c.set cs " +
            "JOIN c.prices p " +
            "WHERE (:games IS NULL OR c.game IN :games) " +
            "AND (:setId IS NULL OR cs.id = :setId) " +
            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR cs.id LIKE %:query% OR c.setNumber LIKE %:query%) " +
            "AND (:condition IS NULL OR p.condition = :condition) " +
            "AND (:finish IS NULL OR p.finish = :finish) " +
            "AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Long> findMatchingCardIds(
            @Param("games") List<CardGame> games,
            @Param("setId") String setId,
            @Param("rarity") CardRarity rarity,
            @Param("condition") CardCondition condition,
            @Param("finish") CardFinish finish,
            @Param("query") String query,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    // Count total matching cards for pagination
    @Query("SELECT COUNT(DISTINCT c.id) FROM Card c " +
            "JOIN c.set cs " +
            "JOIN c.prices p " +
            "WHERE (:games IS NULL OR c.game IN :games) " +
            "AND (:setId IS NULL OR cs.id = :setId) " +
            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR cs.id LIKE %:query% OR c.setNumber LIKE %:query%) " +
            "AND (:condition IS NULL OR p.condition = :condition) " +
            "AND (:finish IS NULL OR p.finish = :finish) " +
            "AND p.price BETWEEN :minPrice AND :maxPrice")
    long countMatchingCardIds(
            @Param("games") List<CardGame> games,
            @Param("setId") String setId,
            @Param("rarity") CardRarity rarity,
            @Param("condition") CardCondition condition,
            @Param("finish") CardFinish finish,
            @Param("query") String query,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    @EntityGraph(attributePaths = {"images", "set", "prices", "pokemonDetails"})
    @Query("SELECT c FROM Card c WHERE c.id IN :ids")
    List<Card> findCardsByIdsSortedByName(
            @Param("ids") List<Long> ids,
            Sort sort
    );

    @EntityGraph(attributePaths = {"images", "set", "prices", "pokemonDetails"})
    @Query("SELECT c FROM Card c " +
            "JOIN c.prices p " +
            "WHERE c.id IN :ids " +
            "AND (:condition IS NULL OR p.condition = :condition) " +
            "AND (:finish IS NOT NULL AND p.finish = :finish " +
            "OR :finish IS NULL AND p.finish IN ('NORMAL', 'HOLOFOIL', 'REVERSE_HOLO', 'STAMP') " +
            "AND NOT EXISTS (SELECT 1 FROM CardPrice p2 WHERE p2.card = c AND " +
            "((p.finish = 'HOLOFOIL' AND p2.finish = 'NORMAL') OR " +
            "(p.finish = 'REVERSE_HOLO' AND p2.finish IN ('NORMAL', 'HOLOFOIL')) OR " +
            "(p.finish = 'STAMP' AND p2.finish IN ('NORMAL', 'HOLOFOIL', 'REVERSE_HOLO')))))")
    List<Card> findCardsByIdsSortedByPrice(
            @Param("ids") List<Long> ids,
            @Param("condition") CardCondition condition,
            @Param("finish") CardFinish finish,
            Sort sort
    );

    @EntityGraph(attributePaths = {"prices", "images", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithBasicDataById(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "prices",
            "rules",
            "subTypes",
            "images",
            "variantGroup",
            "set",
            "pokemonDetails",
            "pokemonDetails.types",
            "pokemonDetails.abilities",
            "pokemonDetails.attacks",
            "pokemonDetails.attacks.cost"
    })
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithAllDataById(Long id);

    /**
     * Fetches a list of Cards by their IDs and eagerly loads their associated
     * collections of prices and images
     */
    @Query("""
        SELECT DISTINCT c FROM Card c
        LEFT JOIN FETCH c.prices
        LEFT JOIN FETCH c.images
        WHERE c.id IN :cardIds
    """)
    Set<Card> findCardsWithDetailsByIds(@Param("cardIds") List<Long> cardIds);
}
