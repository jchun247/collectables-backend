package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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
    private Set<CardTypes> types = new HashSet<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardAttack> attacks = new HashSet<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardPrice> prices = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "card_abilities", joinColumns = @JoinColumn(name = "card_id"))
    private Set<CardAbility> abilities = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "card_subtypes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name="subtype")
    @Enumerated(EnumType.STRING)
    private Set<CardSubType> subTypes = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "card_images", joinColumns = @JoinColumn(name = "card_id"))
    private Set<CardImage> images = new HashSet<>();
}
