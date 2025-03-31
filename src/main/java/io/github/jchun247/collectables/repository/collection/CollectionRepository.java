package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    @Query("SELECT DISTINCT c FROM Collection c " +
            "LEFT JOIN FETCH c.cards " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.valueHistory " +
            "WHERE c.id = :collectionId")
    Optional<Collection> findByIdWithCards(@Param("collectionId") Long collectionId);

    @Query("SELECT DISTINCT c FROM Collection c " +
            "LEFT JOIN FETCH c.cards " +
            "LEFT JOIN FETCH c.valueHistory " +
            "WHERE c.user.id = :targetUserId " +
            "AND (:isOwner = true OR c.isPublic = true)")
    List<Collection> findCollectionsByUserIdAndVisibility(
            @Param("targetUserId") Long targetUserId,
            @Param("isOwner") boolean isOwner
    );
}