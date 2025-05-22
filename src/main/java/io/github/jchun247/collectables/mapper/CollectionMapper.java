package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.card.BasicCardDTO;
import io.github.jchun247.collectables.dto.card.CardPriceDTO;
import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.model.card.Card;
import io.github.jchun247.collectables.model.card.CardImage;
import io.github.jchun247.collectables.model.card.CardImageResolution;
import io.github.jchun247.collectables.model.card.CardPrice;
import io.github.jchun247.collectables.model.collection.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    CollectionDTO toCollectionDto(Collection collection);
    CollectionListDTO toCollectionListDto(CollectionList collectionList);
    PortfolioDTO toPortfolioDto(Portfolio portfolio);

//    @Mapping(source = "card.id", target="cardId")
    @Mapping(source = "collection.id", target="collectionId")
    @Mapping(source = "card", target="card")
    @Mapping(source = "condition", target="condition")
    @Mapping(source = "finish", target="finish")
    @Mapping(source = "quantity", target="quantity")
//    @Mapping(source = "transactionHistories", target="transactionHistories")
    CollectionCardDTO toCollectionCardDto(CollectionCard collectionCard);

    CollectionCardTransactionHistoryDTO toCollectionCardTransactionHistoryDto(CollectionCardTransactionHistory transactionHistory);

    @Mapping(source = "set.code", target = "setId")
    @Mapping(source = "set.name", target = "setName")
    @Mapping(source = "images", target = "imageUrl", qualifiedByName = "mapImageUrl") // Use existing method for imageUrl
    @Mapping(source = "prices", target = "prices") // Map Card.prices (Set<CardPrice>) to BasicCardDTO.prices (Set<CardPriceDTO>)
    BasicCardDTO toBasicCardDto(Card card); // New method to map Card entity to BasicCardDTO

    CardPriceDTO toCardPriceDto(CardPrice cardPrice);

    PortfolioValueHistoryDTO toPortfolioValueHistoryDto(PortfolioValueHistory portfolioValueHistory);

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
}
