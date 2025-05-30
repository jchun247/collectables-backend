package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.model.card.CardSeries;
import io.github.jchun247.collectables.model.card.CardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardSetRepository extends JpaRepository<CardSet, Long> {
    @Query("SELECT cs FROM CardSet cs LEFT JOIN FETCH cs.legalities LEFT JOIN FETCH cs.images WHERE cs.series = :series")
    List<CardSet> findAllBySeriesWithCollections(@Param("series") CardSeries series);
    @Query("SELECT DISTINCT cs.series FROM CardSet cs")
    List<CardSeries> findAllSeries();
}