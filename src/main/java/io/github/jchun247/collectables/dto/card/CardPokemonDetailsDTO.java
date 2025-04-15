package io.github.jchun247.collectables.dto.card;

import io.github.jchun247.collectables.model.card.*;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class CardPokemonDetailsDTO {
    private int hitPoints;
    private int retreatCost;
    private String flavourText;
    private CardWeakness weakness;
    private CardResistance resistance;
    private Set<CardTypesDTO> types = new LinkedHashSet<>();
    private Set<CardAttackDTO> attacks = new LinkedHashSet<>();
    private Set<CardAbilityDTO> abilities = new LinkedHashSet<>();
}
