package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.CollectionCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionCardRepository extends JpaRepository<CollectionCard, Long> {
    List<CollectionCard> findByCollectionId(Long collectionId);
}
