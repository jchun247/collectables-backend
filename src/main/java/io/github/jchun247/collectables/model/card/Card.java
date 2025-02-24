package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    private CardFinish finish;

    private String illustratorName;
    private int hitPoints;
    private String type;
    private String flavourText;
    private int retreatCost;

    @ElementCollection
    @CollectionTable(name = "card_weaknesses", joinColumns = @JoinColumn(name = "card_id"))
    private List<String> weaknesses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "card_resistances", joinColumns = @JoinColumn(name = "card_id"))
    private List<String> resistances = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardAttack> attacks = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "card_abilities", joinColumns = @JoinColumn(name = "card_id"))
    private List<CardAbility> abilities;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardPrice> prices = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "card_images", joinColumns = @JoinColumn(name = "card_id"))
    private Set<CardImage> images = new HashSet<>();

    public void addPrice(CardPrice price) {
        prices.add(price);
        price.setCard(this);
    }

    public void addImage(CardImage image) {
        images.add(image);
    }
}
