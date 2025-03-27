package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.model.card.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long> {
//    @Query("SELECT DISTINCT c FROM Card c " +
//            "LEFT JOIN FETCH c.prices p " +
//            "WHERE (:games IS NULL OR c.game IN :games) " +
//            "AND (:setCode IS NULL OR c.set.code = :setCode) " +
//            "AND (:rarity IS NULL OR c.rarity = :rarity) " +
//            "AND (:condition IS NULL OR p.condition = :condition) " +
//            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query IS NULL OR c.set.code LIKE %:query% OR c.setNumber LIKE %:query%) "
//            "AND p.price BETWEEN :minPrice AND :maxPrice")
//    )
//    Page<Card> findByFilters(
//            @Param("games") List<CardGame> games,
//            @Param("setCode") String setCode,
//            @Param("rarity") CardRarity rarity,
//            @Param("condition") CardCondition condition,
//            @Param("query") String query,
//            @Param("minPrice") Double minPrice,
//            @Param("maxPrice") Double maxPrice,
//            Pageable pageable
//    );

    @EntityGraph(attributePaths = {"prices", "images", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithBasicDataById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"types", "attacks", "prices", "abilities", "subTypes", "images", "variantGroup", "set"})
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Optional<Card> findWithAllDataById(Long id);
}
