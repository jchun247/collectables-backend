package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardGame;
import io.github.jchun247.collectables.model.card.CardRarity;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Valid
public class CreateCardRequestDto {
    private String name;
    private CardGame game;
    private String setCode;
    private String setNumber;
    private CardRarity rarity;
    private Set<CreateCardImageRequestDto> images;
    private List<CreateCardPriceRequestDto> prices;
}
