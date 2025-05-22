package io.github.jchun247.collectables.repository.collection;

import io.github.jchun247.collectables.model.collection.CollectionCardTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionCardTransactionHistoryRepository extends JpaRepository<CollectionCardTransactionHistory, Long> {
    void deleteByCollectionCardId(Long collectionCardId);

    Page<CollectionCardTransactionHistory> findTransactionHistoriesByCollectionCardId(
            Long collectionCardId,
            Pageable pageable
    );
}
