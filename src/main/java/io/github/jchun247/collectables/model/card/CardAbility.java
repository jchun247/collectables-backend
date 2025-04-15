package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="card_abilities")
@Getter
@Setter
@ToString(exclude = "pokemonDetails")
@EqualsAndHashCode(of = "id")
public class CardAbility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String text;
    private String type;

    @ManyToOne
    @JoinColumn(name = "card_pokemon_details_id")
    private CardPokemonDetails pokemonDetails;
}
