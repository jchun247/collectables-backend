package io.github.jchun247.collectables.dto.collection;

import java.math.BigDecimal;

public record PortfolioStatBuildingBlocks(
        Long collectionId,
        long numProducts,
        BigDecimal currentValue,
        BigDecimal totalCostOfBuys,
        BigDecimal totalProceedsFromSells,
        BigDecimal totalRealizedGain
) {
}
