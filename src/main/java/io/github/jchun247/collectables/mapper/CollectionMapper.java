package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.collection.CollectionCardDto;
import io.github.jchun247.collectables.dto.collection.CollectionDto;
import io.github.jchun247.collectables.model.collection.Collection;
import io.github.jchun247.collectables.model.collection.CollectionCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CollectionMapper {
    CollectionDto toCollectionDto(Collection collection);
    CollectionCardDto toCollectionCardDto(CollectionCard collectionCard);
}
