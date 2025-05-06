package io.github.jchun247.collectables.dto.collection;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PortfolioValueHistoryDTO {
    private Long id;
    private BigDecimal value;
    private LocalDateTime timestamp;
}