package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface CollectionCardRepository extends JpaRepository<CollectionCard, Long> {
    Optional<CollectionCard> findByCollectionIdAndCardIdAndConditionAndFinish(
            Long collectionId,
            Long cardId,
            CardCondition condition,
            CardFinish finish
    );

    @Query("""
        SELECT DISTINCT cc FROM CollectionCard cc
        LEFT JOIN FETCH cc.collection col
        LEFT JOIN FETCH cc.card c
        LEFT JOIN FETCH c.set s
        LEFT JOIN FETCH c.pokemonDetails pd
        WHERE cc.id IN :ids
    """)
    List<CollectionCard> findAllByIdsEagerly(@Param("ids") List<Long> ids);

    @Query(value = """
        SELECT
            cc.id as collectionCardId,
            COALESCE(SUM(CASE WHEN t.transactionType = 'BUY' THEN t.quantity ELSE -t.quantity END * cp.price), 0.0) as calculatedTotalStackValue
        FROM CollectionCard cc
        JOIN cc.card c
        INNER JOIN cc.transactionHistories t
        LEFT JOIN c.prices cp ON cp.card.id = c.id AND cp.condition = cc.condition AND cp.finish = cc.finish
        WHERE cc.collection.id = :collectionId
        AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :cardName, '%')) OR :cardName IS NULL)
        GROUP BY cc.id, c.name
       """,
            countQuery = """
        SELECT COUNT(DISTINCT cc.id) /* DISTINCT in case joins multiply rows before GROUP BY in main query */
        FROM CollectionCard cc
        JOIN cc.card c /* Join for card name filtering */
        WHERE cc.collection.id = :collectionId
        AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :cardName, '%')) OR :cardName IS NULL)
    """)
    Page<Object[]> findCollectionCardIdsAndTotalStackValueSorted(
            @Param("collectionId") Long collectionId,
            @Param("cardName") @Nullable String cardName,
            Pageable pageable
    );
}