package io.github.jchun247.collectables.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collection_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Collection collection;
    @ManyToOne
    private Card card;
    private int quantity;
}
