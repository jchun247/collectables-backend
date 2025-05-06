package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.collection.*;
import io.github.jchun247.collectables.model.collection.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    CollectionDTO toCollectionDto(Collection collection);
    CollectionListDTO toCollectionListDto(CollectionList collectionList);
    PortfolioDTO toPortfolioDto(Portfolio portfolio);

    @Mapping(source = "card.id", target="cardId")
    @Mapping(source = "collection.id", target="collectionId")
    CollectionCardDTO toCollectionCardDto(CollectionCard collectionCard);

    PortfolioValueHistoryDTO toPortfolioValueHistoryDto(PortfolioValueHistory portfolioValueHistory);
}
