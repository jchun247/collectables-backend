package io.github.jchun247.collectables.dto;

import io.github.jchun247.collectables.model.CardCondition;
import io.github.jchun247.collectables.model.CardPrice;
import lombok.Data;

@Data
public class CardPriceDto {
    private CardCondition condition;
    private Double price;

    public CardPriceDto(CardPrice price) {
        this.condition = price.getCondition();
        this.price = price.getPrice();
    }
}
