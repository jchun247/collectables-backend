package io.github.jchun247.collectables.model.collection;

import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardCondition;
import io.github.jchun247.collectables.model.card.CardFinish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collection_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    @Enumerated(EnumType.STRING)
    private CardFinish finish;

    @OneToMany(mappedBy = "collectionCard", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<CollectionCardTransactionHistory> transactionHistories = new ArrayList<>();

    public void addTransactionHistory(CollectionCardTransactionHistory transaction) {
        transactionHistories.add(transaction);
        transaction.setCollectionCard(this);
    }

    public void removeTransactionHistory(CollectionCardTransactionHistory transaction) {
        transactionHistories.remove(transaction);
        transaction.setCollectionCard(null);
    }
}
