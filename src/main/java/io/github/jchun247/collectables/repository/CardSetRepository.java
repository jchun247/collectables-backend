package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.card.CardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardSetRepository extends JpaRepository<CardSet, Long> {
    Optional<CardSet> findByCode(String setCode);
}
