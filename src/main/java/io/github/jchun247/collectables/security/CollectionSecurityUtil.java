package io.github.jchun247.collectables.security;

import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.repository.collection.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionSecurityUtil {
    private final CollectionRepository collectionRepository;

    public void validateCollectionOwnership(Long collectionId, String auth0Id) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        if (!collection.getUser().getAuth0Id().equals(auth0Id)) {
            throw new IllegalArgumentException("User does not own collection with id: " + collectionId);
        }
    }
}
