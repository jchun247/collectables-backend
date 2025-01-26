package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionCardRepository extends JpaRepository<CollectionCard, Long> {
    List<CollectionCard> findByCollectionId(Long collectionId);
}
