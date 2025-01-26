package io.github.jchun247.collectables.service.collection;

import io.github.jchun247.collectables.model.collection.Collection;

import java.util.List;

public interface CollectionService {
    Collection createCollection(Collection collection);
    List<Collection> getAllCollections();
    Collection getCollectionById(Long id);
}
