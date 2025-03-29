package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="card_attack_costs")
@Getter
@Setter
public class CardAttackCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attack_id")
    private CardAttack attack;

    @Column(name = "cost")
    private CardEnergy cost;

    @Column(name="cost_order")
    private Integer costOrder;
}
