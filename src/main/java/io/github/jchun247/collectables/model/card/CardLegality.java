package io.github.jchun247.collectables.model.card;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Embeddable
@Data
public class CardLegality {
    @Enumerated(EnumType.STRING)
    private CardSetFormat format;
    private String legality;
}
