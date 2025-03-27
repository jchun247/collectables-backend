package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="card_types")
@Getter
@Setter
@ToString(exclude = "card")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CardTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private CardType type;

    public void setCard(Card card) {
        if (this.card != null) {
            this.card.getTypes().remove(this);
        }
        this.card = card;
        if (card != null) {
            card.getTypes().add(this);
        }
    }
}
