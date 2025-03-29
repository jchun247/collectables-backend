package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="card_attacks")
@Getter
@Setter
@ToString(exclude = "card")
@EqualsAndHashCode(of = "id")
public class CardAttack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private String name;

    @OneToMany(mappedBy = "attack", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("costOrder ASC")
    private List<CardAttackCost> cost = new ArrayList<>();

    private String damage;
    private String text;

    public void setCard(Card card) {
        if (this.card != null) {
            this.card.getAttacks().remove(this);
        }
        this.card = card;
        if (card != null) {
            card.getAttacks().add(this);
        }
    }
}
