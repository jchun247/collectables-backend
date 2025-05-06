package io.github.jchun247.collectables.dto.collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class PortfolioDTO extends CollectionDTO {
    private BigDecimal totalCostBasis;
}
