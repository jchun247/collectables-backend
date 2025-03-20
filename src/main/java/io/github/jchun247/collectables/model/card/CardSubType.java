package io.github.jchun247.collectables.model.card;

import lombok.Getter;

@Getter
public enum CardSubType {
    TEAM_PLASMA("Team Plasma"),
    BREAK("BREAK"),
    V_UNION("V-UNION"),
    VMAX("VMAX"),
    ROCKETS_SECRET_MACHINE("Rocket's Secret Machine"),
    ACE_SPEC("ACE SPEC"),
    LEVEL_UP("Level-Up"),
    POKEMON_TOOL("Pokémon Tool"),
    VSTAR("VSTAR"),
    TECHNICAL_MACHINE("Technical Machine"),
    LEGEND("LEGEND"),
    ANCIENT("Ancient"),
    SPECIAL("Special"),
    TAG_TEAM("TAG TEAM"),
    PRIME("Prime"),
    SUPPORTER("Supporter"),
    RESTORED("Restored"),
    STAR("Star"),
    STADIUM("Stadium"),
    MEGA("MEGA"),
    ULTRA_BEAST("Ultra Beast"),
    SP("SP"),
    RAPID_STRIKE("Rapid Strike"),
    V("V"),
    POKEMON_TOOL_F("Pokémon Tool F"),
    EX("ex"),
    ITEM("Item"),
    STAGE_2("Stage 2"),
    STAGE_1("Stage 1"),
    PRISM_STAR("Prism Star"),
    FUSION_STRIKE("Fusion Strike"),
    BASIC("Basic"),
    RADIANT("Radiant"),
    EX_UPPER("EX"),
    FUTURE("Future"),
    ETERNAMAX("Eternamax"),
    GX("GX"),
    BABY("Baby"),
    SINGLE_STRIKE("Single Strike"),
    GOLDENROD_GAME_CORNER("Goldenrod Game Corner"),
    TERA("Tera");

    private final String databaseValue;

    CardSubType(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static CardSubType fromDatabaseValue(String value) {
        for (CardSubType subType : values()) {
            if (subType.getDatabaseValue().equals(value)) {
                return subType;
            }
        }
        throw new IllegalArgumentException("No CardSubType found for database value: " + value);
    }
}