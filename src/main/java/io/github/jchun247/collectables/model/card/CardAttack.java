package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="card_attacks")
@Getter
@Setter
@ToString(exclude = "pokemonDetails")
@EqualsAndHashCode(of = "id")
public class CardAttack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String damage;
    private String text;

    @ManyToOne
    @JoinColumn(name = "card_pokemon_details_id")
    private CardPokemonDetails pokemonDetails;

    @OneToMany(mappedBy = "attack", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardAttackCost> cost = new LinkedHashSet<>();
}
