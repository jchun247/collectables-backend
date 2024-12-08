package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
