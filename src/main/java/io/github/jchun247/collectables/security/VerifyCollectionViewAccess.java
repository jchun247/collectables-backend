package io.github.jchun247.collectables.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to verify if the authenticated user can VIEW the collection.
 * Access is granted if the collection is public OR if the user owns the collection.
 * Uses Spring Expression Language (SpEL) to invoke the validation logic
 * in CollectionSecurityUtil#canViewCollection.

 * Handles anonymous users by passing null as the auth0Id if authentication is null.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
// SpEL: Calls canViewCollection with collectionId and the principal's name (or null if anonymous)
// Uses safe navigation (?.) to avoid NPE if 'authentication' is null.
// Assumes the parameter holding the ID is named 'collectionId'. Adjust if different (e.g., #portfolioId).
@PreAuthorize("@collectionSecurityUtil.canViewCollection(#collectionId, authentication?.name)")
public @interface VerifyCollectionViewAccess {
}
