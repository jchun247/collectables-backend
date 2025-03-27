package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.CardImageResolution;
import lombok.Data;

@Data
public class CardImageDTO {
    private String url;
    private CardImageResolution resolution;
}
