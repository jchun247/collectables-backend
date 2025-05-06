package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.card.*;
import io.github.jchun247.collectables.model.card.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "set.id", target = "setId")
    @Mapping(source = "images", target="imageUrl", qualifiedByName = "mapImageUrl")
    @Mapping(source = "prices", target = "prices", qualifiedByName = "mapPrices")
    BasicCardDTO toBasicDTO(Card card);

    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "set.id", target = "setId")
    @Mapping(source = "prices", target = "prices", qualifiedByName = "mapPrices")
    @Mapping(source = "priceHistory", target = "priceHistory", qualifiedByName = "mapPriceHistory")
    @Mapping(source = "pokemonDetails", target = "pokemonDetails", qualifiedByName = "mapPokemonDetails")
    @Mapping(source = "pokemonDetails.types", target = "pokemonDetails.types")
    @Mapping(source = "pokemonDetails.attacks", target = "pokemonDetails.attacks")
    @Mapping(source = "pokemonDetails.abilities", target = "pokemonDetails.abilities")
    CardDTO toCardDTO(Card card);

    BasicCardSetDTO toBasicCardSetDTO(CardSet cardSet);

    @Named("mapImageUrl")
    default String mapImageUrl(Set<CardImage> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
                .filter(image -> image.getResolution() == CardImageResolution.LOW_RES)
                .findFirst()
                .map(CardImage::getUrl)
                .orElse(null);
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

    @Named("mapPokemonDetails")
    default CardPokemonDetailsDTO mapPokemonDetails(CardPokemonDetails cardPokemonDetails) {
        if (cardPokemonDetails == null) {
            return null;
        }
        return toCardPokemonDetailsDTO(cardPokemonDetails);
    }

//    CardVariantGroupDTO toCardVariantGroupDTO(CardVariantGroup variantGroup);

    CardPriceDTO toCardPriceDTO(CardPrice price);

    CardPriceHistoryDTO toCardPriceHistoryDTO(CardPriceHistory priceHistory);

    CardPokemonDetailsDTO toCardPokemonDetailsDTO(CardPokemonDetails cardPokemonDetails);
}