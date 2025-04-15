package io.github.jchun247.collectables.dto.card;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class CardAttackDTO {
    private Long id;
    private String name;
    private String text;
    private String damage;
    private Set<CardAttackCostDTO> cost = new LinkedHashSet<>();
}
