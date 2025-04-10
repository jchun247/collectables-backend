package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "card_pokemon_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPokemonDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "card_id")
    private Card card;

    private Integer hitPoints;
    private Integer retreatCost;
    private String flavourText;

    @Embedded
    private CardWeakness weakness;

    @Embedded
    private CardResistance resistance;

    @OneToMany(mappedBy = "pokemonDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardTypes> types = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pokemonDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardAttack> attacks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pokemonDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardAbility> abilities = new LinkedHashSet<>();
}
