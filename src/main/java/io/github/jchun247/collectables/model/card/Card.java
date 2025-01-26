package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private CardGame game;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private CardSet set;
    private String setNumber;

    @Enumerated(EnumType.STRING)
    private CardRarity rarity;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardPrice> prices = new ArrayList<>();

    private String imageUrl;

    public void addPrice(CardPrice price) {
        prices.add(price);
        price.setCard(this);
    }
}
