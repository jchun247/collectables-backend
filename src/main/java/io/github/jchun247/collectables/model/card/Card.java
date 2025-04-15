package io.github.jchun247.collectables.model.card;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "cards")
@Getter
@Setter
@ToString(exclude = {"variantGroup", "set", "prices", "priceHistory", "rules", "pokemonDetails"})
@EqualsAndHashCode(of = {"id", "externalId"})
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

    @Column(name="supertype")
    private CardSuperType superType;

    private String illustratorName;
    private String setNumber;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CardPokemonDetails pokemonDetails;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardRule> rules = new LinkedHashSet<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardPrice> prices = new HashSet<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardPriceHistory> priceHistory = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "card_subtypes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name="subtype")
    private Set<CardSubType> subTypes = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "card_images", joinColumns = @JoinColumn(name = "card_id"))
    private Set<CardImage> images = new HashSet<>();
}
