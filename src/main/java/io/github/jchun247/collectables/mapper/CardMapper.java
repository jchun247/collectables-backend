package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.card.*;
import io.github.jchun247.collectables.model.card.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "card.prices", target = "prices")
    BasicCardDTO toBasicDTO(Card card);

//    @Mapping(source = "set.name", target = "setName")
//    @Mapping(source = "types", target = "typeNames")
//    DetailedCardDTO toDetailedDTO(Card card);

    // Custom mappings for collections and complex fields
//    default List<String> typesToTypeNames(List<CardTypes> types) {
//        if (types == null) {
//            return Collections.emptyList();
//        }
//        return types.stream()
//                .map(CardTypes::getTypeName)
//                .collect(Collectors.toList());
//    }

    CardImageDTO toCardImageDTO(CardImage image);

    CardAttackDTO toCardAttackDTO(CardAttack attack);

//    CardWeaknessDTO toCardWeaknessDTO(CardWeakness weakness);
//
//    CardResistanceDTO toCardResistanceDTO(CardResistance resistance);
//
//    CardVariantGroupDTO toCardVariantGroupDTO(CardVariantGroup variantGroup);
//
    CardPriceDTO toCardPriceDTO(CardPrice price);
//
//    CardAbilityDTO toCardAbilityDTO(CardAbility ability);
}