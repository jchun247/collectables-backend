package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
