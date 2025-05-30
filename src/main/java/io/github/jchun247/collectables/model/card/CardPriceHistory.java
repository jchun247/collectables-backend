package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="card_price_history")
@Getter
@Setter
@ToString(exclude = "card")
@EqualsAndHashCode(of = {"id", "condition", "finish"})
@NoArgsConstructor
public class CardPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    @Enumerated(EnumType.STRING)
    private CardFinish finish;

    private BigDecimal price;
    private LocalDateTime timestamp;

    public void setCard(Card card) {
        if (this.card != null) {
            this.card.getPriceHistory().remove(this);
        }
        this.card = card;
        if (card != null) {
            this.card.getPriceHistory().add(this);
        }
    }
}