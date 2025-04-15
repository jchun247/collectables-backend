package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="card_rules")
@Getter
@Setter
@ToString(exclude = "card")
@EqualsAndHashCode(of = "id")
public class CardRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private String text;
}
