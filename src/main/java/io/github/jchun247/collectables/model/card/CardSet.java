package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name="sets")
@Data
@NoArgsConstructor
public class CardSet {
    @Id
    private String id;

    @Column(unique = true)
    private String code;

    private String name;
    private LocalDate releaseDate;
    private LocalDateTime lastUpdated;
    private int printedTotal;
    private int total;

    @Enumerated(EnumType.STRING)
    private CardGame game;

    @Enumerated(EnumType.STRING)
    private CardSeries series;

    @ElementCollection
    @CollectionTable(name = "set_legalities", joinColumns = @JoinColumn(name = "set_id"))
    private Set<CardLegality> legalities;

    @ElementCollection
    @CollectionTable(name = "set_images", joinColumns = @JoinColumn(name = "set_id"))
    private Set<CardSetImage> images = new HashSet<>();
}
