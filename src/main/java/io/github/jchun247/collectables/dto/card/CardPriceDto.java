package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardPrice;
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
