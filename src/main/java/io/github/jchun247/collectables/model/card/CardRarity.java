package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardRarity {
    HYPER_RARE("Hyper Rare"),
    RARE("Rare"),
    RARE_HOLO_LVX("Rare Holo LV.X"),
    SPECIAL_ILLUSTRATION_RARE("Special Illustration Rare"),
    RARE_HOLO_STAR("Rare Holo Star"),
    ILLUSTRATION_RARE("Illustration Rare"),
    SHINY_ULTRA_RARE("Shiny Ultra Rare"),
    RARE_SHINY("Rare Shiny"),
    RARE_HOLO_GX("Rare Holo GX"),
    LEGEND("LEGEND"),
    CLASSIC_COLLECTION("Classic Collection"),
    DOUBLE_RARE("Double Rare"),
    RARE_BREAK("Rare BREAK"),
    RARE_HOLO_V("Rare Holo V"),
    RARE_PRISM_STAR("Rare Prism Star"),
    ACE_SPEC_RARE("ACE SPEC Rare"),
    RARE_RAINBOW("Rare Rainbow"),
    COMMON("Common"),
    RADIANT_RARE("Radiant Rare"),
    RARE_PRIME("Rare Prime"),
    RARE_HOLO_VSTAR("Rare Holo VSTAR"),
    AMAZING_RARE("Amazing Rare"),
    RARE_HOLO_VMAX("Rare Holo VMAX"),
    RARE_SECRET("Rare Secret"),
    RARE_ULTRA("Rare Ultra"),
    UNCOMMON("Uncommon"),
    RARE_ACE("Rare ACE"),
    SHINY_RARE("Shiny Rare"),
    ULTRA_RARE("Ultra Rare"),
    RARE_HOLO_EX("Rare Holo EX"),
    RARE_HOLO("Rare Holo"),
    RARE_SHINY_GX("Rare Shiny GX"),
    RARE_SHINING("Rare Shining"),
    TRAINER_GALLERY_RARE_HOLO("Trainer Gallery Rare Holo"),
    PROMO("Promo");

    private final String databaseValue;

    CardRarity(String databaseValue) {
        this.databaseValue = databaseValue;
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

