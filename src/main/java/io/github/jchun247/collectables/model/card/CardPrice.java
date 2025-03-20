package io.github.jchun247.collectables.model.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="card_price")
@Data
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
}
