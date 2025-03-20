package io.github.jchun247.collectables.model.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CardImage {
    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private CardImageResolution resolution;
}
