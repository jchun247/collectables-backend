package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BasicCardSetDTO {
    private String id;
    private String name;
    private CardGame game;
    private LocalDate releaseDate;
    private CardSeries series;
    private Set<CardSetImage> images;
}