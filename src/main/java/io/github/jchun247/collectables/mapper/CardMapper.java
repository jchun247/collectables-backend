package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.card.*;
import io.github.jchun247.collectables.model.card.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Getter
    @AllArgsConstructor
    class PriceFilterContext {
        private final CardCondition condition;
        private final List<CardFinish> finishes;
    }

    @Mapping(source = "card.set.name", target = "setName")
    @Mapping(source = "card.set.id", target = "setId")
    @Mapping(source = "card.images", target = "imageUrl", qualifiedByName = "mapImageUrl")
    @Mapping(source = "card", target = "prices", qualifiedByName = "mapFilteredPrices")
    BasicCardDTO toBasicDTO(Card card, @Context PriceFilterContext filterContext);

    @Named("mapFilteredPrices")
    default Set<CardPriceDTO> mapFilteredPrices(Card card, @Context PriceFilterContext filterContext) {
        Set<CardPrice> prices = card.getPrices();
        if (prices == null) {
            return Collections.emptySet();
        }

        CardCondition condition = filterContext.getCondition();
        List<CardFinish> finishes = filterContext.getFinishes();

        return prices.stream()
                .filter(price -> (condition == null || price.getCondition() == condition) &&
                        (finishes == null || finishes.isEmpty() || finishes.contains(price.getFinish())))
                .map(this::toCardPriceDTO)
                .collect(Collectors.toSet());
    }

    // --- Other methods ---

    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "set.id", target = "setId")
    @Mapping(source = "prices", target = "prices", qualifiedByName = "mapPrices")
    CardDTO toCardDTO(Card card);

    BasicCardSetDTO toBasicCardSetDTO(CardSet cardSet);
    CardPriceHistoryDTO toCardPriceHistoryDTO(CardPriceHistory priceHistory);
    CardPriceDTO toCardPriceDTO(CardPrice price);
    CardPokemonDetailsDTO toCardPokemonDetailsDTO(CardPokemonDetails cardPokemonDetails);

    @Named("mapImageUrl")
    default String mapImageUrl(Set<CardImage> images) {
        if (images == null || images.isEmpty()) { return null; }
        return images.stream()
                .filter(image -> image.getResolution() == CardImageResolution.LOW_RES)
                .findFirst().map(CardImage::getUrl).orElse(null);
    }

    @Named("mapPrices")
    default Set<CardPriceDTO> mapPrices(Set<CardPrice> prices) {
        if (prices == null) { return Collections.emptySet(); }
        return prices.stream().map(this::toCardPriceDTO).collect(Collectors.toSet());
    }
}