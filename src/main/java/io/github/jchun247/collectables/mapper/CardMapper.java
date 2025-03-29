package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.card.*;
import io.github.jchun247.collectables.model.card.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "prices", target = "prices", qualifiedByName = "mapPrices")
    BasicCardDTO toBasicDTO(Card card);

    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "types", target = "types", qualifiedByName = "mapTypes")
    @Mapping(source = "attacks", target = "attacks", qualifiedByName = "mapAttacks")
    @Mapping(source = "prices", target = "prices", qualifiedByName = "mapPrices")
    @Mapping(source = "priceHistory", target = "priceHistory", qualifiedByName = "mapPriceHistory")
    CardDTO toCardDTO(Card card);

    @Mapping(source = "cost", target = "cost", qualifiedByName = "mapAttackCosts")
    CardAttackDTO toCardAttackDTO(CardAttack attack);

    @Named("mapTypes")
    default Set<CardTypesDTO> mapTypes(Set<CardTypes> types) {
        if (types == null) {
            return Collections.emptySet();
        }
        return types.stream()
                .map(this::toCardTypesDTO)
                .collect(Collectors.toSet());
    }

    @Named("mapAttacks")
    default Set<CardAttackDTO> mapAttacks(Set<CardAttack> attacks) {
        if (attacks == null) {
            return Collections.emptySet();
        }
        return attacks.stream()
                .map(this::toCardAttackDTO)
                .collect(Collectors.toSet());
    }

    @Named("mapPrices")
    default Set<CardPriceDTO> mapPrices(Set<CardPrice> prices) {
        if (prices == null) {
            return Collections.emptySet();
        }
        return prices.stream()
                .map(this::toCardPriceDTO)
                .collect(Collectors.toSet());
    }

    @Named("mapPriceHistory")
    default Set<CardPriceHistoryDTO> mapPriceHistory(Set<CardPriceHistory> priceHistory) {
        if (priceHistory == null) {
            return Collections.emptySet();
        }
        return priceHistory.stream()
                .map(this::toCardPriceHistoryDTO)
                .collect(Collectors.toSet());
    }

    @Named("mapAttackCosts")
    default List<CardEnergy> mapAttackCosts(List<CardAttackCost> costs) {
        if (costs == null) {
            return Collections.emptyList();
        }
        return costs.stream()
                .sorted(Comparator.comparing(CardAttackCost::getCostOrder))
                .map(CardAttackCost::getCost)
                .collect(Collectors.toList());
    }

    CardTypesDTO toCardTypesDTO(CardTypes type);

//    CardVariantGroupDTO toCardVariantGroupDTO(CardVariantGroup variantGroup);

    CardPriceDTO toCardPriceDTO(CardPrice price);

    CardPriceHistoryDTO toCardPriceHistoryDTO(CardPriceHistory priceHistory);

}