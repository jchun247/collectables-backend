package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="card_attacks")
@Data
public class CardAttack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private String name;

    @ElementCollection
    @CollectionTable(name = "card_attack_costs", joinColumns = @JoinColumn(name = "attack_id"))
    private List<String> cost = new ArrayList<>();

    private String damage;
    private String text;
}
