package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    @Override
    public Collection createCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    @Override
    public List<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    @Override
    public Collection getCollectionById(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + id));
    }
}
