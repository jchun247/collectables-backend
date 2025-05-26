package io.github.jchun247.collectables.dto.collection;

import java.math.BigDecimal;

public record CollectionStats(
        Long collectionId,
        long numProducts,
        BigDecimal currentValue,
        BigDecimal totalCostBasis) {
}
