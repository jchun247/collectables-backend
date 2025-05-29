package io.github.jchun247.collectables.model.collection;

import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "collection_card_transaction_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionCardTransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_card_id")
    private CollectionCard collectionCard;

    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    @Enumerated(EnumType.STRING)
    private CardFinish finish;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private int quantity;
    private LocalDate purchaseDate;
    private BigDecimal costBasis;
    private BigDecimal realizedGain;
}
