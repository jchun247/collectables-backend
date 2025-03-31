package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    List<Collection> findAllByUserId(Long userId);
}