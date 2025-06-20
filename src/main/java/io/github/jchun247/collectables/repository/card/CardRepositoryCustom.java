package io.github.jchun247.collectables.repository.card;

import io.github.jchun247.collectables.dto.card.CardSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {
    Page<Long> findCardIdsByCriteria(CardSearchCriteria criteria, Pageable pageable);
}