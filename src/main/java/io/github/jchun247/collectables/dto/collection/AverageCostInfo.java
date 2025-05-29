package io.github.jchun247.collectables.dto.collection;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record AverageCostInfo(BigDecimal totalBuyCost, long totalBuyQuantity) {
    public BigDecimal getAverageCost() {
        if (totalBuyQuantity <= 0 || totalBuyCost == null || totalBuyCost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalBuyCost.divide(new BigDecimal(totalBuyQuantity), 4, RoundingMode.HALF_UP);
    }
}
