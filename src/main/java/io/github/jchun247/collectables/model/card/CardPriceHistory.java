package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_price_history")
@Data
@NoArgsConstructor
public class CardPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_price_id", nullable = false)
    private CardPrice cardPrice;

    private BigDecimal price;
    private LocalDateTime timestamp;
}