package io.github.jchun247.collectables.model;

import io.github.jchun247.collectables.model.card.CardCondition;
import lombok.Data;

@Data
public class CreateCardPriceRequest {
    private CardCondition condition;
    private Double price;
}
