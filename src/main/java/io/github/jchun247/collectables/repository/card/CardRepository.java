package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.model.card.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
//    @Query(value = "SELECT DISTINCT c.id FROM Card c " +
//            "JOIN c.set cs " +
//            "JOIN c.prices p " +
//            "WHERE (:games IS NULL OR c.game IN :games) " +
//            "AND (:setId IS NULL OR cs.id = :setId) " +
//            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
//            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR cs.id LIKE %:query% OR c.setNumber LIKE %:query%) " +
//            "AND p.condition = :condition " +
//            "AND (:finish IS NULL OR p.finish = :finish) " +
//            "AND p.price BETWEEN :minPrice AND :maxPrice")
//    Page<Long> findCardIds(
//            @Param("games") List<CardGame> games,
//            @Param("setId") String setId,
//            @Param("rarity") CardRarity rarity,
//            @Param("condition") CardCondition condition,
//            @Param("finish") CardFinish finish,
//            @Param("query") String query,
//            @Param("minPrice") BigDecimal minPrice,
//            @Param("maxPrice") BigDecimal maxPrice,
//            Pageable pageable
//    );

//    @Query(value = "SELECT c.id FROM Card c " +
//            "JOIN c.set cs " +
//            "JOIN c.prices p " +
//            "WHERE (:games IS NULL OR c.game IN :games) " +
//            "AND (:setId IS NULL OR cs.id = :setId) " +
//            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
//            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR cs.id LIKE %:query% OR c.setNumber LIKE %:query%) " +
//            "AND p.condition = :condition " +
//            "AND (:finish IS NULL OR p.finish = :finish) " +
//            "AND p.price BETWEEN :minPrice AND :maxPrice")
//    Page<Long> findCardIdsSortedByPrice(
//            @Param("games") List<CardGame> games,
//            @Param("setId") String setId,
//            @Param("rarity") CardRarity rarity,
//            @Param("condition") CardCondition condition,
//            @Param("finish") CardFinish finish,
//            @Param("query") String query,
//            @Param("minPrice") BigDecimal minPrice,
//            @Param("maxPrice") BigDecimal maxPrice,
//            Pageable pageable
//    );
    /**
     * Takes a list of Card IDs and fetches the full Card objects with their collections.
     */
    @EntityGraph(attributePaths = {"images", "set", "prices", "pokemonDetails"})
    @Query("SELECT c FROM Card c WHERE c.id IN :ids")
    List<Card> findFullCardsByIds(@Param("ids") List<Long> ids);


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
