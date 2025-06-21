package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardRarity {
    COMMON("Common", 10),
    UNCOMMON("Uncommon", 20),
    RARE("Rare", 30),
    RARE_PRISM_STAR("Rare Prism Star", 40),
    RARE_HOLO("Rare Holo", 50),
    RARE_ULTRA("Rare Ultra", 60),
    ULTRA_RARE("Ultra Rare", 70),
    RARE_HOLO_STAR("Rare Holo Star", 80),
    RARE_HOLO_EX("Rare Holo EX", 90),
    RARE_HOLO_LVX("Rare Holo LV.X", 100),
    RARE_PRIME("Rare Prime", 110),
    LEGEND("LEGEND", 120),
    RARE_BREAK("Rare BREAK", 130),
    RARE_HOLO_GX("Rare Holo GX", 140),
    RARE_HOLO_V("Rare Holo V", 150),
    RARE_HOLO_VMAX("Rare Holo VMAX", 160),
    RARE_HOLO_VSTAR("Rare Holo VSTAR", 170),
    DOUBLE_RARE("Double Rare", 180),
    AMAZING_RARE("Amazing Rare", 190),
    RADIANT_RARE("Radiant Rare", 200),
    RARE_SHINING("Rare Shining", 201),
    ACE_SPEC_RARE("ACE SPEC Rare", 210),
    RARE_ACE("Rare ACE", 220),
    CLASSIC_COLLECTION("Classic Collection", 230),
    RARE_SHINY("Rare Shiny", 240),
    SHINY_RARE("Shiny Rare", 250),
    RARE_SHINY_GX("Rare Shiny GX", 260),
    TRAINER_GALLERY_RARE_HOLO("Trainer Gallery Rare Holo", 270),
    ILLUSTRATION_RARE("Illustration Rare", 280),
    SPECIAL_ILLUSTRATION_RARE("Special Illustration Rare", 290),
    RARE_SECRET("Rare Secret", 300),
    RARE_RAINBOW("Rare Rainbow", 310),
    HYPER_RARE("Hyper Rare", 320),
    SHINY_ULTRA_RARE("Shiny Ultra Rare", 330),
    PROMO("Promo", 340);

    private final String databaseValue;
    private final int sortOrder;

    CardRarity(String databaseValue, int sortOrder) {
        this.databaseValue = databaseValue;
        this.sortOrder = sortOrder;
    }

    public static CardRarity fromDatabaseValue(String value) {
        for (CardRarity rarity : values()) {
            if (rarity.getDatabaseValue().equals(value)) {
                return rarity;
            }
        }
        throw new IllegalArgumentException("No CardRarity found for database value: " + value);
    }
}

