package io.github.jchun247.collectables.model.collection;

import io.github.jchun247.collectables.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name="is_public")
    private boolean isPublic;
    @Column(name="num_products")
    private int numProducts;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CollectionValueHistory> valueHistory;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CollectionCard> cards;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private UserEntity user;

//    public BigDecimal calculateCurrentValue() {
//        return cards.stream()
//                .map(collectionCard -> collectionCard.getCard().getPrices().stream()
//                        .filter(price -> price.getCondition() == collectionCard.getCondition())
//                        .findFirst()
//                        .map(price -> price.getPrice().multiply(BigDecimal.valueOf(collectionCard.getQuantity())))
//                        .orElse(BigDecimal.ZERO))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
}
