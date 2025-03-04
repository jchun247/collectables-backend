package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "card_variant_groups")
@Data
public class CardVariantGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "variantGroup")
    private Set<Card> cards = new HashSet<>();
}
