package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.card.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT DISTINCT t FROM Card t " +
            "JOIN t.prices p " +
            "WHERE (:game IS NULL OR t.game LIKE %:game%) " +
            "AND (:set IS NULL OR t.set = :set%) " +
            "AND (:rarity IS NULL OR t.rarity = :rarity) " +
            "AND (:condition IS NULL OR p.condition = :condition) " +
            "AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Card> findByFilters(
            @Param("game") CardGame game,
            @Param("set") CardSet set,
            @Param("rarity") CardRarity rarity,
            @Param("condition") CardCondition condition,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
