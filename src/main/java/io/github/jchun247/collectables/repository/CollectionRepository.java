package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);
}
