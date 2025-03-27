package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="card_price")
@Getter
@Setter
@ToString(exclude = "card")
@EqualsAndHashCode(of = {"id", "condition", "finish"})
@NoArgsConstructor
@AllArgsConstructor
public class CardPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    @Enumerated(EnumType.STRING)
    private CardFinish finish;

    private BigDecimal price;
    private LocalDateTime updatedAt;

    public void setCard(Card card) {
        if (this.card != null) {
            this.card.getPrices().remove(this);
        }
        this.card = card;
        if (card != null) {
            card.getPrices().add(this);
        }
    }
}
