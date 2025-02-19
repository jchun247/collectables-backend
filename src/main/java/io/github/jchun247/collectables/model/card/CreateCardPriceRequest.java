package io.github.jchun247.collectables.model.card;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCardPriceRequest {
    private CardCondition condition;
    private BigDecimal price;
}
