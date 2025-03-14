package io.github.jchun247.collectables.model.card;

import io.github.jchun247.collectables.converter.CardRarityConverter;
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

    private String externalId;
    private String name;

    @ManyToOne
    @JoinColumn(name="card_variant_group_id")
    private CardVariantGroup variantGroup;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private CardSet set;

    @Enumerated(EnumType.STRING)
    private CardGame game;

    private CardRarity rarity;

    private String illustratorName;
    private String flavourText;
    private String setNumber;
    private int hitPoints;
    private int retreatCost;

    @Embedded
    private CardWeakness weakness;

    @Embedded
    private CardResistance resistance;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardTypes> types = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardAttack> attacks = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardPrice> prices = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "card_abilities", joinColumns = @JoinColumn(name = "card_id"))
    private List<CardAbility> abilities;

    @ElementCollection
    @CollectionTable(name = "card_subtypes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name="subtype")
    @Enumerated(EnumType.STRING)
    private List<CardSubType> subTypes;

    @ElementCollection
    @CollectionTable(name = "card_images", joinColumns = @JoinColumn(name = "card_id"))
    private Set<CardImage> images = new HashSet<>();

//    public void addPrice(CardPrice price) {
//        prices.add(price);
//        price.setCard(this);
//    }

//    public void addImage(CardImage image) {
//        images.add(image);
//    }

//    public void addVariant(Card variant) {
//        variant.setParentCard(this);
//        variants.add(variant);
//    }

//    public boolean isVariant() {
//        return parentCard != null;
//    }
//
//    public boolean isBaseCard() {
//        return parentCard == null;
//    }
}
