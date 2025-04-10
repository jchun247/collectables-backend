package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="card_types")
@Getter
@Setter
@ToString(exclude = "pokemonDetails")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CardTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_pokemon_details_id")
    private CardPokemonDetails pokemonDetails;

    private CardType type;

//    public void setPokemonDetails(CardPokemonDetails pokemonDetails) {
//        if (this.pokemonDetails != null) {
//            this.pokemonDetails.getTypes().remove(this);
//        }
//        this.pokemonDetails = pokemonDetails;
//        if (pokemonDetails != null) {
//            pokemonDetails.getTypes().add(this);
//        }
//    }
}
