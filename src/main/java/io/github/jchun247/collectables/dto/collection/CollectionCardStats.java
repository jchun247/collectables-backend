package io.github.jchun247.collectables.dto.collection;

import java.math.BigDecimal;

public record CollectionCardStats(
        Long collectionCardId,
        long totalQuantity,
        BigDecimal currentValue,
        BigDecimal totalCostOfBuys,
        BigDecimal totalProceedsFromSells,
        BigDecimal totalRealizedGain
) {
}

