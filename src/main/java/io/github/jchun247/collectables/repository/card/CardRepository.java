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
            "LEFT JOIN c.prices p " +
            "LEFT JOIN c.set cs " +
            "WHERE (:games IS NULL OR c.game IN :games) " +
            "AND (:setCode IS NULL OR cs.code = :setCode) " +
            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
            "AND (:condition IS NULL OR p.condition = :condition) " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR cs.code LIKE %:query% OR c.setNumber LIKE %:query%) " +
            "AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Long> findCardIdsByFilters(
            @Param("games") List<CardGame> games,
            @Param("setCode") String setCode,
            @Param("rarity") CardRarity rarity,
            @Param("condition") CardCondition condition,
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
    List<Card> findCardsWithCollectionsByIds(@Param("ids") List<Long> ids);


    @EntityGraph(attributePaths = {"prices", "images", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithBasicDataById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"types", "attacks", "prices", "priceHistory", "abilities", "subTypes", "images", "variantGroup", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithAllDataById(Long id);
}
