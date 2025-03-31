package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CardSetDTO {
    private String code;
    private String name;
    private CardGame game;
    private LocalDate releaseDate;
    private LocalDateTime lastUpdated;
    private CardSeries series;
    private Set<CardLegality> legalities;
    private Set<CardSetImage> images;

    public static CardSetDTO fromEntity(CardSet cardSet) {
        return CardSetDTO.builder()
                .code(cardSet.getCode())
                .name(cardSet.getName())
                .game(cardSet.getGame())
                .releaseDate(cardSet.getReleaseDate())
                .lastUpdated(cardSet.getLastUpdated())
                .series(cardSet.getSeries())
                .legalities(cardSet.getLegalities())
                .images(cardSet.getImages())
                .build();
    }
}