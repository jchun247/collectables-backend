package io.github.jchun247.collectables.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashboardDTO {
    private int numCards;
    private BigDecimal collectionValue;
    private BigDecimal highestValueCard;
}
