package io.github.jchun247.collectables.security;

import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.repository.collection.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionSecurityUtil {
    private final CollectionRepository collectionRepository;

    @Transactional(readOnly = true)
    public void validateCollectionOwnership(Long collectionId, String auth0Id) {
        if (collectionId == null || auth0Id == null) {
            log.warn("Attempted ownership validation with null collectionId or auth0Id.");
            throw new AccessDeniedException("Cannot verify ownership without collectionId and user ID.");
        }

        log.debug("Validating ownership for collection ID: {} by user: {}", collectionId, auth0Id);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> {
                    log.warn("Ownership validation failed: Collection not found with id: {}", collectionId);
                    return new ResourceNotFoundException("Collection not found with id: " + collectionId);
                });

        if (!collection.getUser().getAuth0Id().equals(auth0Id)) {
            log.warn("Access denied for user {} on collection {}. Owner is {}.",
                    auth0Id, collectionId, collection.getUser().getAuth0Id());
            throw new AccessDeniedException("User does not own collection with id: " + collectionId);
        }
        log.debug("Ownership validated successfully for collection ID: {} by user: {}", collectionId, auth0Id); // Added success log
    }

    @Transactional(readOnly = true)
    public boolean canViewCollection(Long collectionId, String auth0Id) {
        if (collectionId == null) {
            log.warn("Attempted view validation with null collectionId.");
            return false;
        }

        log.debug("Checking view access for collection ID: {} by user: {}", collectionId, auth0Id == null ? "Anonymous" : auth0Id);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> {
                    log.warn("View access check failed: Collection not found with id: {}", collectionId);
                    return new ResourceNotFoundException("Collection not found with id: " + collectionId);
                });

        // Allow access if the collection is public
        if (collection.isPublic()) {
            log.debug("View access granted for collection ID: {} (Public Collection)", collectionId);
            return true;
        }

        // If private, allow access only if the user is the owner
        // Use Objects.equals to safely handle null auth0Id (anonymous user)
        boolean isOwner = auth0Id != null && Objects.equals(collection.getUser().getAuth0Id(), auth0Id);
        if (isOwner) {
            log.debug("View access granted for collection ID: {} (User is Owner)", collectionId);
        } else {
            log.warn("View access denied for user {} on private collection {}. Owner is {}.",
                    auth0Id == null ? "Anonymous" : auth0Id, collectionId, collection.getUser().getAuth0Id());
        }
        return isOwner;
    }
}
