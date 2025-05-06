package io.github.jchun247.collectables.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom annotation for ownership verification
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@PreAuthorize("@collectionSecurityUtil.validateCollectionOwnership(#collectionId, authentication.name)")
public @interface VerifyCollectionAccess {
}