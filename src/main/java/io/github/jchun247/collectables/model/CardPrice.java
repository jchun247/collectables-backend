package io.github.jchun247.collectables.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="card_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    private Double price;
}
