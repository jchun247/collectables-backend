package io.github.jchun247.collectables.model.card;

import lombok.Data;

@Data
public class CreateCardPriceRequest {
    private CardCondition condition;
    private Double price;
}
